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
        miner.getTxPool().setTransactions(new ArrayList());
        BlockController.validate(candidateBlock, Main.Main.DIFFICULTY);
        return candidateBlock;
    }

    public static void broadCastBlock(Miner miner, Block candidateBlock) {
        //BROAD CAST BLOCK
    }

    public static void incommingTransaction(Miner miner, Transaction transaction) {
        TransactionPoolController.addTransaction(miner.getTxPool(), transaction);
    }
}
