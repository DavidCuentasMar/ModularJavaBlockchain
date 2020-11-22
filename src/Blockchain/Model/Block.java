package Blockchain.Model;

import Main.Main;
import Utils.HashUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class Block {

    public int idCadena;
    public int index;
    public int nonce;
    public String previousHash;
    public String hash;
    public String merkleRoot;
    @JsonDeserialize(as=ArrayList.class, contentAs=Transaction.class)
    public ArrayList<Transaction> transactions;
    //@JsonDeserialize(as = LocalDateTime.class)
    public String timestamp;
    private Random r;

    @JsonCreator
    public Block(@JsonProperty("idCadena") int idCadena, @JsonProperty("index") int index, @JsonProperty("nonce") int nonce,
            @JsonProperty("previousHash") String previousHash, @JsonProperty("hash") String hash, @JsonProperty("merkleRoot") String merkleRoot,
            @JsonProperty("transactions") ArrayList transactions, @JsonProperty("timestamp") String timestamp) {
        this.idCadena = idCadena;
        this.index = index;
        this.nonce = nonce;
        this.previousHash = previousHash;
        this.hash = hash;
        this.merkleRoot = merkleRoot;
        this.transactions = transactions;
        this.timestamp = timestamp;

    }

    public Block(int index, String timestamp, ArrayList transactions,
            String previousHash, String merkleRoot) {
        this(index, timestamp, transactions, previousHash, Main.DIFFICULTY, merkleRoot);
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public Block(int index, String timestamp, ArrayList transactions, String previousHash, int difficulty, String merkleRoot) {
        r = new Random();
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.nonce = r.nextInt();
        this.hash = HashUtils.calculateHash(this.toString4Hash());
        this.merkleRoot = merkleRoot;
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

    @JsonIgnore
    public String getTimestamp() {
        return timestamp;
    }
    
    //@JsonProperty("timestamp")
    @JsonIgnore
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    @JsonProperty("transactions")
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

    @JsonIgnore
    public Random getR() {
        return r;
    }

    public void setR(Random r) {
        this.r = r;
    }

    public void renonce() {
        this.nonce = r.nextInt();
    }

    public void rehash() {
        this.hash = HashUtils.calculateHash(toString4Hash());
    }

    public String toString4Hash() {
        StringBuffer s = new StringBuffer();
        s.append(index);
        s.append(timestamp);
        s.append(transactions);
        s.append(previousHash);
        s.append(nonce);
        return s.toString();
    }

    public boolean verifyHash(int difficulty) {
        if (this.hash.equals(HashUtils.calculateHash(this.toString4Hash()))) {
            return true;
        }
        return false;
    }
}
