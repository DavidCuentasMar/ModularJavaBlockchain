package Blockchain.Model;

import java.util.ArrayList;

public class TransactionPool {
    ArrayList<Transaction> transactions;
    
    public TransactionPool() {
        transactions = new ArrayList();
    }

    public void addTransaction(Transaction t){
        this.transactions.add(t);
        
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
    
}
