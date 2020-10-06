package Blockchain.Model;

import Utils.HashUtils;

public class Transaction {

    String from_address;
    String to_address;
    String[] data;
    String hash;
    String signature;

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
