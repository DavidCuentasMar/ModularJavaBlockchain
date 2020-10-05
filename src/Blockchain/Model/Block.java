package Blockchain.Model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
public class Block {
    int idCadena;
    int index;
    LocalDateTime timestamp;
    ArrayList<Transaction> transactions;
    String previousHash;
    String hash;
    int nonce;
    Random r;
    
    public Block(int index, LocalDateTime timestamp, ArrayList transactions, String previousHash, int difficulty) {
        r = new Random();
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.nonce = r.nextInt();
        //TO-DO hash
    }

    public int getIdCadena() {
        return idCadena;
    }

    public void setIdCadena(int idCadena) {
        this.idCadena = idCadena;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public Random getR() {
        return r;
    }

    public void setR(Random r) {
        this.r = r;
    }
    
}
