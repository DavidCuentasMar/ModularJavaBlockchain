package Blockchain.Model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Chain {
    private static int idCount=0;
    private static Semaphore mutex = new Semaphore(1);
    ArrayList<Block> chain;
    int id;
    int difficulty;
    
}
