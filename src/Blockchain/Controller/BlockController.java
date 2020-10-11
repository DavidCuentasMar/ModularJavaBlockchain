package Blockchain.Controller;

import Blockchain.Model.Block;
import Utils.HashUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class BlockController {

    public static Block createNewBlock(int index, LocalDateTime timestamp, ArrayList transactions, String previousHash) {
        Block newBlock = new Block(index, timestamp, transactions, previousHash);
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
        System.out.println(j + " intentos");
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
}
