package Main;

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
import rice.environment.Environment;

//tiempo simulacioon, tiempo real
public class Main {

    public static final int DIFFICULTY = 3;

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException, UnsupportedEncodingException, Exception {
        Boolean blockchainLogicToTest = true;
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

            //Chain creation
            Chain theChain = new Chain();
            theChain.addGenesisBlock();

            //Miners Creation
            Miner miner1 = new Miner(new TransactionPool());
            Miner miner2 = new Miner(new TransactionPool());

            //KeyPair Creation
            KeyPair keyPairA = DigitalSignature.generateKeyPair();
            PublicKey publicKey = keyPairA.getPublic();
            PrivateKey privateKey = keyPairA.getPrivate();

            //Transaction Creation
            Transaction tx1 = new Transaction("addrx1", "addrx2", new String[]{"A"});
            Transaction tx2 = new Transaction("addrx2", "addrx3", new String[]{"B"});
            Transaction tx3 = new Transaction("addrx3", "addrx4", new String[]{"C"});
            Transaction tx4 = new Transaction("addrx4", "addrx5", new String[]{"D"});
            Transaction tx8 = new Transaction("addrx8", "addrx9", new String[]{"H"});
            Transaction tx9 = new Transaction("addrx9", "addrx10", new String[]{"I"});
            Transaction tx10 = new Transaction("addrx10", "addrx11", new String[]{"J"});

            ArrayList<Transaction> transactionGroupOne = new ArrayList();
            transactionGroupOne.add(tx1);
            transactionGroupOne.add(tx2);
            transactionGroupOne.add(tx3);
            transactionGroupOne.add(tx4);

            ArrayList<Transaction> transactionGroupTwo = new ArrayList();
            transactionGroupTwo.add(tx8);
            transactionGroupTwo.add(tx9);
            transactionGroupTwo.add(tx10);

            //Signing Transaction
            for (Transaction tx : transactionGroupOne) {
                TransactionController.signTransaction(tx, publicKey, privateKey);
            }

            //Check Transaction
            for (Transaction tx : transactionGroupOne) {
                TransactionController.checkTransaction(tx, publicKey);
            }

            //Sending Transaction to Miners
            for (Transaction tx : transactionGroupOne) {
                MinerController.incommingTransaction(miner1, tx);
            }

            //Miner Generates Candidate Block
            Block minerBlock = MinerController.GenerateCandiateBock(miner1, theChain.getChainSize(), theChain.getLastBlock().getHash());
            MinerController.broadCastBlock(miner1, minerBlock);
            theChain.addBlock(minerBlock);

            //Sending Transaction to Miners
            for (Transaction tx : transactionGroupTwo) {
                MinerController.incommingTransaction(miner1, tx);
            }

            //Miner Generates Candidate Block
            Block minerBlock2 = MinerController.GenerateCandiateBock(miner1, theChain.getChainSize(), theChain.getLastBlock().getHash());
            MinerController.broadCastBlock(miner1, minerBlock2);
            theChain.addBlock(minerBlock2);

            theChain.listAllBlocks();
        }
    }
}
