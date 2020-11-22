package Main;

import Blockchain.Controller.BlockController;
import Blockchain.Controller.MinerController;
import Blockchain.Controller.TransactionController;
import Blockchain.Model.Block;
import Blockchain.Model.Chain;
import Blockchain.Model.Miner;
import Blockchain.Model.Transaction;
import Blockchain.Model.TransactionPool;
import Utils.DigitalSignature;
import Utils.JsonParser;
import Utils.PastryPeer2Peer;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import rice.environment.Environment;

//tiempo simulacioon, tiempo real
public class Main {

    public static final int DIFFICULTY = 1;

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException,
            InvalidKeyException, UnsupportedEncodingException, Exception {
        Boolean blockchainLogicToTest = false;
        if (!blockchainLogicToTest) {
            Environment env = new Environment();

            env.getParameters().setString("nat_search_policy", "never");

            try {
                // the port to use locally
                int bindport = Integer.parseInt(args[0]);

                // build the bootaddress from the command line args
                InetAddress bootaddr = InetAddress.getByName(args[1]);
                int bootport = Integer.parseInt(args[2]);
                InetSocketAddress bootaddress = new InetSocketAddress(bootaddr, bootport);

                // launch our node!
                PastryPeer2Peer dt = new PastryPeer2Peer(bindport, bootaddress, env);
            } catch (Exception e) {
                System.out.println("How to use it?");
                System.out.println("java -jar ModularJavaBlockchain.jar localbindport bootIP bootPort");
                System.out.println("example java -jar ModularJavaBlockchain.jar 9000 192.168.1.1 9000");
                throw e;
            }
        } else {

            // KeyPair Creation
            KeyPair keyPairA = DigitalSignature.generateKeyPair();
            PublicKey publicKey = keyPairA.getPublic();
            PrivateKey privateKey = keyPairA.getPrivate();
            String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());

            // Chain creation
            Chain theChain = new Chain();
            theChain.addGenesisBlock(publicKeyStr, privateKey);

            // Miners Creation
            Miner miner1 = new Miner(new TransactionPool());
            Miner miner2 = new Miner(new TransactionPool());

            // Transaction Creation
            Transaction tx1 = new Transaction(publicKeyStr, "JavaContractCoin", new String[] { "addrx2", "10.0" });
            Transaction tx2 = new Transaction(publicKeyStr, "JavaContractCoin", new String[] { "addrx3", "10.0" });

            ArrayList<Transaction> transactionGroupOne = new ArrayList();
            transactionGroupOne.add(tx1);

            ArrayList<Transaction> transactionGroupTwo = new ArrayList();
            transactionGroupTwo.add(tx2);

            // Signing Transaction
            for (Transaction tx : transactionGroupOne) {
                TransactionController.signTransaction(tx, privateKey);
            }

            // Signing Transaction
            for (Transaction tx : transactionGroupTwo) {
                TransactionController.signTransaction(tx2, privateKey);
            }

            // Sending Transaction to Miners (Broadcast TXS)
            for (Transaction tx : transactionGroupOne) {
                MinerController.incommingTransaction(miner1, tx);
            }

            // Miner Generates Candidate Block
            Block minerBlock = MinerController.GenerateCandiateBock(miner1, theChain);
            MinerController.broadCastBlock(miner1, minerBlock);
            if (minerBlock != null) {
                theChain.addBlock(minerBlock);
            }
            System.out.println(miner1.getTxPool().getTransactions().size());
            // Sending Transaction to Miners (Broadcast TXS)
            for (Transaction tx : transactionGroupTwo) {
                MinerController.incommingTransaction(miner1, tx);
            }
            System.out.println(miner1.getTxPool().getTransactions().size());
            // Miner Generates Candidate Block
            minerBlock = MinerController.GenerateCandiateBock(miner1, theChain);
            MinerController.broadCastBlock(miner1, minerBlock);
            if (minerBlock != null) {
                theChain.addBlock(minerBlock);
            }

            theChain.listAllBlocks();
            
            
            String newchain = JsonParser.chainToJson(theChain);
            Chain otherChain = JsonParser.jsonToChain(newchain);
            
            otherChain.listAllBlocks();
        }
    }
}
