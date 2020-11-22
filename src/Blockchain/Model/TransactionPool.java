package Blockchain.Model;

import Utils.JsonParser;
import java.util.ArrayList;

public class TransactionPool {
    ArrayList<Transaction> transactions;
    
    public TransactionPool() {
        transactions = new ArrayList();
    }

    public void addTransaction(Transaction t){
        this.transactions.add(t);
    }
    
    public void showTransactions () {
        System.out.println("tamaño del pool: "+transactions.size());
        for (Transaction tx: transactions){
            String strTx = JsonParser.transactionToJson(tx);
            System.out.println(strTx);
        }
    }

    public void clear(){
        transactions.clear();
    }
    
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void clearTxPool() {
        this.transactions.clear();
    }
    
}
