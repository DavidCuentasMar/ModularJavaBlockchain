package Blockchain.Model;

import Blockchain.Controller.BlockController;
import Blockchain.Controller.TransactionController;
import Main.Main;
import Utils.ConfigController;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chain {

    private static int idCount = 0;
    private static Semaphore mutex = new Semaphore(1);
    @JsonDeserialize(as=ArrayList.class, contentAs=Block.class)
    public ArrayList<Block> chain;

    public ArrayList<Block> getChain() {
        return chain;
    }
    public int id;
    public int difficulty;

    public Chain() {
        this(ConfigController.readConfigJson().difficulty);
    }
    @JsonCreator
    public Chain(@JsonProperty("difficulty") int difficulty, @JsonProperty("id") int id, @JsonProperty("chain") ArrayList chain){
        this.difficulty = difficulty;
        this.id = id;
        this.chain = chain;
    }
    
    public Chain(int difficulty) {
        chain = new ArrayList();
        id = idCount++;
        this.difficulty = difficulty;
        //addGenesisBlock();
        System.out.println("Blockchain " + id + " created successfuly!");
    }

    public void addGenesisBlock(String publicKeyStr, PrivateKey privateKey) { 
        try {
            String amount = ConfigController.readConfigJson().initialAmount;
            Transaction tx0 = new Transaction(publicKeyStr, "JavaContractCoin", new String[]{publicKeyStr, amount});
            TransactionController.signTransaction(tx0, privateKey);
            ArrayList<Transaction> txs = new ArrayList();
            txs.add(tx0);
            Block b = BlockController.createNewBlock(0, LocalDateTime.now().toString(), txs, "0");
            BlockController.validate(b, difficulty);
            chain.add(b);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Chain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(Chain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized boolean addBlock(Block b) {
        try {
            mutex.acquire();
            if (b.verifyHash(difficulty)) {
                Block ant = this.getLastBlock();
                b.setPreviousHash(ant.getHash());
                b.setIndex(chain.size());
                BlockController.validate(b, difficulty);
                chain.add(b);
                mutex.release();
                return true;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Chain.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Failed to add new block " + id);
        return false;
    }
    @JsonIgnore
    public Block getLastBlock() { 
        return chain.get(chain.size() - 1);
    }
    @JsonIgnore
    public int getChainSize(){
        return this.chain.size();
    }
    public void listAllBlocks() {
        System.out.println("Lista de bloques - Blockchain" + id + ":");
        System.out.println("---");
        for (Block b : chain) {
            System.out.println("Index: " + b.getIndex());
            System.out.println("Previous Hash:" + b.getPreviousHash());
            System.out.println("Hash:" + b.getHash());
            System.out.println("Merkle Root: " + b.getMerkleRoot());
            System.out.println("Timestamp: " + b.getTimestamp());
            System.out.println("# of Transactions: " + b.getTransactions().size());
            System.out.println("---");
        }
    }

}
