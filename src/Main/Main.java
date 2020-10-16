package Main;

import Blockchain.Controller.MinerController;
import Blockchain.Controller.TransactionController;
import Blockchain.Model.Block;
import Blockchain.Model.Chain;
import Blockchain.Model.Miner;
import Blockchain.Model.Transaction;
import Blockchain.Model.TransactionPool;
import Utils.DigitalSignature;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;

public class Main {
    public static final int DIFFICULTY = 3;
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException, UnsupportedEncodingException {
        //Chain creation
        Chain theChain = new Chain();
        
        //Miners Creation
        Miner miner1 = new Miner(new TransactionPool());
        Miner miner2 = new Miner(new TransactionPool());
        
        //KeyPair Creation
        KeyPair keyPairA = DigitalSignature.generateKeyPair();
        PublicKey publicKey = keyPairA.getPublic();
        PrivateKey privateKey = keyPairA.getPrivate();                
        
        //Transaction Creation
        Transaction tx1 = new Transaction("addrx1","addrx2",new String[]{"A"});
        Transaction tx2 = new Transaction("addrx2","addrx3",new String[]{"B"});
        Transaction tx3 = new Transaction("addrx3","addrx4",new String[]{"C"});
        
        //Signing Transaction      
        TransactionController.signTransaction(tx1, publicKey, privateKey);
                
        //Check Transaction
        TransactionController.checkTransaction(tx1,publicKey);
        
        //Sending Transaction to Miners
        MinerController.incommingTransaction(miner1, tx1);
        MinerController.incommingTransaction(miner1, tx2);
        MinerController.incommingTransaction(miner1, tx3);

        //Miner Generates Candidate Block
        Block minerBlock = MinerController.GenerateCandiateBock(miner1, theChain.getChainSize(), theChain.getLastBlock().getHash());
        System.out.println(minerBlock);
        MinerController.broadCastBlock(miner1, minerBlock);
        theChain.addBlock(minerBlock);
        
        MinerController.incommingTransaction(miner2, new Transaction("addrx8","addrx9",new String[]{"E"}));
        MinerController.incommingTransaction(miner2, new Transaction("addrx9","addrx10",new String[]{"F"}));
        MinerController.incommingTransaction(miner2, new Transaction("addrx10","addrx11",new String[]{"G"}));

        //Miner Generates Candidate Block
        Block minerBlock2 = MinerController.GenerateCandiateBock(miner2, theChain.getChainSize(), theChain.getLastBlock().getHash());
        MinerController.broadCastBlock(miner2, minerBlock2);
        theChain.addBlock(minerBlock2);
        
        theChain.listAllBlocks();
    }
}
