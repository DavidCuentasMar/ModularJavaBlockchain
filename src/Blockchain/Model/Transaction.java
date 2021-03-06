package Blockchain.Model;

import Utils.HashUtils;
import Utils.MerkleTrees;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    @JsonProperty
    public String from_address;
    @JsonProperty
    public String to_address;
    @JsonProperty
    public String[] data;
    @JsonProperty
    public String hash;
    @JsonProperty
    public String signature;
    @JsonProperty
    public String timestamp;

    @JsonCreator
    public Transaction(@JsonProperty("from_address") String from_address, @JsonProperty("to_address") String to_address,
            @JsonProperty("data") String[] data, @JsonProperty("hash") String hash,
            @JsonProperty("signature") String signature) {
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
        this.timestamp = LocalDateTime.now().toString();
        this.hash = this.callMerkleTreesCreator();
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

    private String callMerkleTreesCreator() {
        List<String> tempTxList = new ArrayList<String>();
        tempTxList.add(from_address);
        tempTxList.add(to_address);
        tempTxList.add(data[0]);
        tempTxList.add(data[1]);
        tempTxList.add(signature);
        tempTxList.add(timestamp);

        MerkleTrees merkleTrees = new MerkleTrees(tempTxList);
        merkleTrees.merkle_tree();

        return merkleTrees.getRoot();
    }
}
