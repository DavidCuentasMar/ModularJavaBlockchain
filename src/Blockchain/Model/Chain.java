package Blockchain.Model;

import Main.Main;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chain {

    private static int idCount = 0;
    private static Semaphore mutex = new Semaphore(1);
    ArrayList<Block> chain;
    int id;
    int difficulty;

    public Chain() {
        this(Main.DIFFICULTY);
    }

    public Chain(int difficulty) {
        chain = new ArrayList();
        id = idCount++;
        this.difficulty = difficulty;
        addGenesisBlock();
        System.out.println("Blockchain " + id + " created successfuly!");
    }

    public void addGenesisBlock() {
        Transaction tx0 = new Transaction("addrx1", "contractAddress", new String[]{"Destiny", "10.0"});
        ArrayList<Transaction> txs = new ArrayList();
        Block b = new Block(0, LocalDateTime.now(), txs, "0");
        b.validate(difficulty);
        chain.add(b);
    }

    public synchronized boolean addBlock(Block b) {

        try {
            mutex.acquire();
            if (b.verifyHash(difficulty)) {
                Block ant = this.getLastBlock();
                b.setPreviousHash(ant.getHash());
                b.setIndex(chain.size());
                b.validate(difficulty);
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

    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    public void listAllBlocks() {
        System.out.println("Lista de bloques - Blockchain" + id + ":");
        System.out.println("---");
        for (Block b : chain) {
            System.out.println("Index: " + b.getIndex());
            System.out.println("Previous Hash:" + b.getPreviousHash());
            System.out.println("Hash:" + b.getHash());
            System.out.println("Timestamp: " + b.getTimestamp());
            System.out.println("---");
        }
    }

}
