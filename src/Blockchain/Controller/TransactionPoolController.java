package Blockchain.Controller;

import Blockchain.Model.Transaction;
import Blockchain.Model.TransactionPool;

public class TransactionPoolController {
    static void addTransaction(TransactionPool txPool, Transaction transaction) {
        txPool.addTransaction(transaction);
    }

}
