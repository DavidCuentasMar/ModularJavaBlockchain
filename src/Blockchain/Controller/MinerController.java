package Blockchain.Controller;

import Blockchain.Model.Block;
import Blockchain.Model.Miner;
import Blockchain.Model.Transaction;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MinerController {

    public static Block GenerateCandiateBock(Miner miner, int index, String previousHash) {
        Block candidateBlock = BlockController.createNewBlock(index,
                LocalDateTime.now(),
                miner.getTxPool().getTransactions(),
                previousHash);
        checkTransactions(candidateBlock.getTransactions());
        BlockController.validate(candidateBlock, Main.Main.DIFFICULTY);
        return candidateBlock;
    }

    private static void checkTransactions(ArrayList<Transaction> transactions) {
        for (Transaction tx : transactions) {
            //TO DO: Validity of Transactions
        }
    }
    
    public static void broadCastBlock(Miner miner, Block candidateBlock){
        //BROAD CAST BLOCK
    }
    

    public static void incommingTransaction(Miner miner, Transaction transaction) {
        TransactionPoolController.addTransaction(miner.getTxPool(), transaction);
    }
}   
