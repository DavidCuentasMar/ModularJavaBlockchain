package Main;

import Blockchain.Model.Block;
import Blockchain.Model.Chain;
import Blockchain.Model.Miner;
import Blockchain.Model.Transaction;
import Blockchain.Model.TransactionPool;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static final int DIFFICULTY = 3;
    public static void main(String[] args) {
        //Chain creation
        Chain theChain = new Chain();
        //Create Genesis Block 
        Block b = new Block(0, LocalDateTime.now(), new ArrayList(), "0");
        theChain.addBlock(b);
        Block b2 = new Block(0, LocalDateTime.now(), new ArrayList(), "0");
        theChain.addBlock(b2);
        theChain.listAllBlocks();

    }
}
