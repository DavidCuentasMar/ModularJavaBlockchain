package Blockchain.Model;

import Utils.HashUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {

    public String from_address;
    public String to_address;
    public String[] data;
    public String hash;
    public String signature;
    
    @JsonCreator
    public Transaction(@JsonProperty("from_address") String from_address, @JsonProperty("to_address") String to_address,
            @JsonProperty("data") String[] data, @JsonProperty("hash") String hash, @JsonProperty("signature") String signature) {
        this.from_address = from_address;
        this.to_address = to_address;
        this.data = data;
        this.hash = hash;
        this.signature = signature;
    }
    
    public Transaction(String from_address, String to_address, String[] data) {
        this.from_address = from_address;
        this.to_address = to_address;
        this.data = data;
        this.hash = HashUtils.calculateHash(this.toString4Hash());
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String[] getData() {
        return data;
    }
    
    @JsonProperty("data")
    public void setData(String[] data) {
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    private String toString4Hash() {
        StringBuffer s = new StringBuffer();
        s.append(from_address);
        s.append(to_address);
        s.append(data);
        return s.toString();
    }

}
