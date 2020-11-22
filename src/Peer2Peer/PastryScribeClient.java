package Peer2Peer;

import Blockchain.Controller.MinerController;
import Blockchain.Controller.TransactionController;
import Blockchain.Model.Block;
import Blockchain.Model.Chain;
import Blockchain.Model.Miner;
import Blockchain.Model.Transaction;
import Blockchain.Model.TransactionPool;
import Utils.DigitalSignature;
import Utils.JsonParser;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import rice.p2p.commonapi.*;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.CancellableTask;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.p2p.scribe.Scribe;
import rice.p2p.scribe.ScribeClient;
import rice.p2p.scribe.ScribeContent;
import rice.p2p.scribe.ScribeImpl;
import rice.p2p.scribe.Topic;
import rice.pastry.commonapi.PastryIdFactory;

/**
 *
 * @author Yennifer Herrera
 */
public class PastryScribeClient implements ScribeClient, Application {

    Scribe myScribe;
    Topic myTopic;
    protected Endpoint endpoint;
    Chain chain = null;
    Miner miner;
    public PublicKey publicKey;
    private PrivateKey privateKey;
    PastryScribeClient client;
    boolean REQUEST_CHAIN = false;
    CancellableTask publishTask;
    public String publicKeyStr;
    private ArrayList<String> listPublicKeys = new ArrayList<>();

    public PastryScribeClient(Node node, Boolean isGenesis) {
        // Genesis node
        System.out.println("Nodo génesis: crear cadena");
        this.endpoint = node.buildEndpoint(this, "myinstance");
        myScribe = new ScribeImpl(node, "myScribeInstance");
        // construct the topic
        myTopic = new Topic(new PastryIdFactory(node.getEnvironment()), "Mining");
        System.out.println("myTopic = " + myTopic);
        // Key generation
        KeyPair keyPairA = DigitalSignature.generateKeyPair();
        publicKey = keyPairA.getPublic();
        privateKey = keyPairA.getPrivate();
        this.publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        if (isGenesis) {
            this.chain = new Chain();
            this.chain.addGenesisBlock(publicKeyStr, privateKey);
        }
        this.miner = new Miner(new TransactionPool());

        try {
            Transaction tx1 = new Transaction(publicKeyStr, "JavaContractCoin", new String[]{"addxr1", "10.0"});
            TransactionController.signTransaction(tx1, privateKey);
            MinerController.incommingTransaction(miner, tx1);
            Block minerBlock = MinerController.GenerateCandiateBock(miner, chain);
            chain.addBlock(minerBlock);
            chain.listAllBlocks();
        } catch (Exception ex) {
            Logger.getLogger(PastryScribeClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        // now we can receive messages
        endpoint.register();
        subscribe();
        //startPublishTask();
    }

    public PastryScribeClient(Node node) {
        System.out.println("Nodo minero: Solicitando cadena");
        this.endpoint = node.buildEndpoint(this, "myinstance");
        myScribe = new ScribeImpl(node, "myScribeInstance");
        // construct the topic
        myTopic = new Topic(new PastryIdFactory(node.getEnvironment()), "Mining");
        System.out.println("myTopic = " + myTopic);
        this.miner = new Miner(new TransactionPool());

        // Key generation
        KeyPair keyPairA = DigitalSignature.generateKeyPair();
        publicKey = keyPairA.getPublic();
        privateKey = keyPairA.getPrivate();
        this.publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        REQUEST_CHAIN = true;
        
        // now we can receive messages
        endpoint.register();
        // Miner
        subscribe();

    }

    private void subscribe() {
        myScribe.subscribe(myTopic, this);
        System.out.println("Subscribed to topic " + myTopic);
    }

    public void startPublishTask() {
        publishTask = endpoint.scheduleMessage(new PublishContent(), 10000, 10000);
    }

    public void unsuscribe() {
        myScribe.unsubscribe(myTopic, this);
    }

    @Override
    public boolean anycast(Topic topic, ScribeContent msg) {
        if (isChainRequest(((PastryScribeContent) msg))) {
            return handlerChainRequest();
        }
        return false;
    }

    public boolean isChainRequest(PastryScribeContent msg) {
        return msg.content.equals("CHAIN_REQUEST") && (msg.type == PastryScribeContent.contentType.TEXT);
    }

    public boolean handlerChainRequest() {
        boolean hasChain = chain != null;
        if (hasChain) {
            System.out.println("HasChain: " + hasChain);
            String jsonChain = JsonParser.chainToJson(this.chain);
            sendChain(jsonChain);
        }
        return hasChain;
    }

    @Override
    public void childAdded(Topic arg0, NodeHandle arg1) {
        // child added
    }

    @Override
    public void childRemoved(Topic arg0, NodeHandle arg1) {
        // child removed
    }

    @Override
    public void subscribeFailed(Topic arg0) {
        System.out.println("Something happened. Subscribe failed");
    }

    @Override
    public boolean forward(RouteMessage arg0) {
        return true;
    }

    @Override
    public void deliver(Topic topic, ScribeContent msg) {
        if (isChainDelivery((PastryScribeContent) msg)) {
            handlerChainDelivery((PastryScribeContent) msg);
        } else if (isBlockDelivery((PastryScribeContent) msg)) {
            handlerBlockDelivery((PastryScribeContent) msg);
        } else if (isTransactionDelivery((PastryScribeContent) msg)) {
            handlerTransactionDelivery((PastryScribeContent) msg);
        } else if (isPublicKeyDelivery((PastryScribeContent) msg)) {
            validatePublicKey((PastryScribeContent) msg);
        }

        if (((PastryScribeContent) msg).from == null) {
            new Exception("Stack Trace").printStackTrace();
        }
    }

    public void validatePublicKey(PastryScribeContent msg) {
        String publicKey = msg.content;
        System.out.println("in comming: " + publicKey);
        if (!publicKey.equals(publicKeyStr)) {
            if (!listPublicKeys.contains(publicKey)) {
                listPublicKeys.add(publicKey);
                int currentSize = listPublicKeys.size();
                System.out.println("currentSize: " + currentSize);
                routeMsgDirect(msg.from, new PastryScribeContent(endpoint.getLocalNodeHandle(), publicKeyStr,
                        PastryScribeContent.contentType.PUBLIC_KEY));
            }
        }
    }

    public boolean isChainDelivery(PastryScribeContent msg) {
        return (this.REQUEST_CHAIN == true && (msg.type == PastryScribeContent.contentType.CHAIN));
    }

    public boolean isPublicKeyDelivery(PastryScribeContent msg) {
        return (msg.type == PastryScribeContent.contentType.PUBLIC_KEY);
    }

    public void handlerChainDelivery(PastryScribeContent chain) {
        boolean hasChain = this.chain == null;
        if (hasChain) {
            this.chain = JsonParser.jsonToChain(chain.content);
            System.out.println("Cadena obtenida");
        }
    }

    public boolean isBlockDelivery(PastryScribeContent msg) {
        return msg.type == PastryScribeContent.contentType.BLOCK;
    }

    public void handlerBlockDelivery(PastryScribeContent block) {
        Block newBlock = JsonParser.jsonToBlock(block.content);
        if (!newBlock.previousHash.isEmpty()) {
            System.out.println("Before size: " + this.chain.getChainSize());
            this.chain.addBlock(newBlock);
            System.out.println("Current size: " + this.chain.getChainSize());
            System.out.println("Block added to chain");
        }
    }

    public boolean isTransactionDelivery(PastryScribeContent msg) {
        return msg.type == PastryScribeContent.contentType.TRANSACTION;
    }

    public void handlerTransactionDelivery(PastryScribeContent trans) {
        Transaction tx = JsonParser.jsonToTransaction(trans.content);
        System.out.println("Transaction hash: " + tx.hash);
        MinerController.incommingTransaction(this.miner, tx);
        System.out.println("Transaction added to miner pool");
        
        if(this.miner.getTxPool().getTransactions().size() == 6){
            Block minerBlock = MinerController.GenerateCandiateBock(this.miner, this.chain);
            if (minerBlock != null) {
                //minando
                this.chain.addBlock(minerBlock);
                System.out.println("paso el minado !!!!");
            }
        }
        //this.miner.getTxPool().showTransactions();
    }

    @Override
    public void update(NodeHandle arg0, boolean arg1) {

    }

    @Override
    public void deliver(Id id, Message message) {
        if (message instanceof PublishContent) {
            PublishContent msg = (PublishContent) message;
            if (msg.content == null) {
                Transaction tx = generateRandomTransaction();
                if (tx != null) {
                    sendTransaction(JsonParser.transactionToJson(tx));
                }
            } else {
                listPublicKeys.add(msg.content.content);
            }
        }
    }

    public void requestChain() {
        if (myScribe.containsTopic(myTopic)) {
            System.out.println("Node " + endpoint.getLocalNodeHandle() + " requesting chain ");
            PastryScribeContent myMessage = new PastryScribeContent(endpoint.getLocalNodeHandle(), "CHAIN_REQUEST",
                    PastryScribeContent.contentType.TEXT);
            myScribe.anycast(myTopic, myMessage);
        } else {
            System.out.println("Ups. Parece que aún no te has suscrito.");
        }
    }

    public void sharePublicKeyToTest() {
        if (myScribe.containsTopic(myTopic)) {
            System.out.println("Node " + endpoint.getLocalNodeHandle() + " sending public key");
            PastryScribeContent myMessage = new PastryScribeContent(endpoint.getLocalNodeHandle(), publicKeyStr,
                    PastryScribeContent.contentType.PUBLIC_KEY);
            myScribe.publish(myTopic, myMessage);
        } else {
            System.out.println("Ups. Parece que aún no te has suscrito.");
        }
    }

    public void sendChain(String chain) {
        if (myScribe.containsTopic(myTopic)) {
            System.out.println("Node " + endpoint.getLocalNodeHandle() + " sending chain");
            PastryScribeContent myMessage = new PastryScribeContent(endpoint.getLocalNodeHandle(), chain,
                    PastryScribeContent.contentType.CHAIN);
            myScribe.publish(myTopic, myMessage);
        } else {
            System.out.println("Ups. Parece que aún no te has suscrito.");
        }
    }

    public void sendTransaction(String trans) {
        if (myScribe.containsTopic(myTopic)) {
            System.out.println("Node " + endpoint.getLocalNodeHandle() + " sending transaction");
            PastryScribeContent myMessage = new PastryScribeContent(endpoint.getLocalNodeHandle(), trans,
                    PastryScribeContent.contentType.TRANSACTION);
            myScribe.publish(myTopic, myMessage);
        } else {
            System.out.println("Ups. Parece que aún no te has suscrito.");
        }
    }

    public void sendBlock(String block) {
        if (myScribe.containsTopic(myTopic)) {
            System.out.println("Node " + endpoint.getLocalNodeHandle() + " sending block");
            PastryScribeContent myMessage = new PastryScribeContent(endpoint.getLocalNodeHandle(), block,
                    PastryScribeContent.contentType.BLOCK);
            myScribe.publish(myTopic, myMessage);
        } else {
            System.out.println("Ups. Parece que aún no te has suscrito.");
        }
    }

    public Transaction generateRandomTransaction() {
        int currentSize = listPublicKeys.size();
        System.out.println("currentSize: " + currentSize);
        if (currentSize > 0) {
            System.out.println("generate tx");
            Random r = new Random();
            int selectedId = r.nextInt(currentSize);
            String selectedKey = listPublicKeys.get(selectedId);

            double amount = 1.0 + (200.0 - 1.0) * r.nextDouble();
            int addr = r.nextInt(20);
            Transaction newTx = new Transaction(publicKeyStr, "JavaContractCoin", new String[]{selectedKey, Double.toString(amount)});
            try {
                TransactionController.signTransaction(newTx, privateKey);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(PastryScribeClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(PastryScribeClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            return newTx;
        }
        return null;
    }

    public void routeMsgDirect(NodeHandle nh, PastryScribeContent content) {
        System.out.println(this + " sending direct to " + nh);
        PublishContent msg = new PublishContent(content);
        endpoint.route(null, msg, nh);
    }
}

class PublishContent implements Message {

    PastryScribeContent content = null;

    public PublishContent() {

    }

    public PublishContent(PastryScribeContent content) {
        this.content = content;
    }

    public int getPriority() {
        return MAX_PRIORITY;
    }
}
