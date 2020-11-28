package Blockchain.Controller;

import Blockchain.Model.Block;
import Blockchain.Model.Transaction;
import Utils.HashUtils;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockController {

    public static Block createNewBlock(int index, String timestamp, ArrayList transactions, String previousHash) {
        String merkleRoot = "";
        merkleRoot = generateMerkleRoot(transactions);
        Block newBlock = new Block(index, timestamp, transactions, previousHash, merkleRoot);
        newBlock.setHash(HashUtils.calculateHash(toString4Hash(newBlock)));
        validate(newBlock, 3);
        return newBlock;
    }

    public static String validate(Block block, int difficulty) {
        int j = 0;
        String s = getDifficultyString(difficulty);
        while (!(block.getHash().substring(0, difficulty).startsWith(s))) {
            block.renonce(); //Finde a new nonce
            block.rehash();  //Recalculate the hash value
            j++;
        }
        return block.getHash();
    }

    public static String getDifficultyString(int difficulty) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < difficulty; i++) {
            s.append("0");
        }
        return s.toString();
    }

    private static String toString4Hash(Block newBlock) {
        StringBuffer s = new StringBuffer();
        s.append(newBlock.getIndex());
        s.append(newBlock.getTimestamp());
        s.append(newBlock.getTransactions());
        s.append(newBlock.getPreviousHash());
        s.append(newBlock.getNonce());
        return s.toString();
    }

    private static String generateMerkleRoot(ArrayList<Transaction> transactions) {
        String root;
        ArrayList<String> txHashes = new ArrayList<String>();
        for (Transaction tx : transactions) {
            txHashes.add(tx.getHash());
        }
        ArrayList<String> newTxList = getNewHashesList(txHashes);
        while (newTxList.size() != 1) {
            newTxList = getNewHashesList(newTxList);
        }
        return 	newTxList.get(0);

    }

    private static ArrayList<String> getNewHashesList(ArrayList<String> txHashes) {

        ArrayList<String> newHashesList = new ArrayList<String>();
        int index = 0;
        while (index < txHashes.size()) {
            // left
            String left = txHashes.get(index);
            index++;

            // right
            String right = "";
            if (index != txHashes.size()) {
                right = txHashes.get(index);
            }

            // sha2 hex value
            String sha2HexValue = getSHA2HexValue(left + right);
            newHashesList.add(sha2HexValue);
            index++;

        }

        return newHashesList;
    }

    private static String getSHA2HexValue(String str) {
        byte[] cipher_byte;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            cipher_byte = md.digest();
            StringBuilder sb = new StringBuilder(2 * cipher_byte.length);
            for (byte b : cipher_byte) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
