package Blockchain.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Miner {
    public TransactionPool txPool;
    public Miner(TransactionPool txPool) {
        this.txPool = txPool;
    }
    public void ReceiveTransaction(Transaction Tx){
        this.txPool.addTransaction(Tx);
    }

    public Block GenerateCandiateBock() {
        Block b = new Block(0, LocalDateTime.now(), txPool.getTransactions(), "");
        checkTransactions(b.transactions);
        return b;
    }

    public void broadCastBlock(Block minerBlock) {
        //TODO: PASTRY SEND MSG
    }

    private void checkTransactions(ArrayList<Transaction> transactions) {
        for (Transaction tx : transactions) {
            //TO DO: Validity of Transactions
        }
    }
}
