package Blockchain.Model;

import Blockchain.Controller.BlockController;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Miner {
    public TransactionPool txPool;
    public Miner(TransactionPool txPool) {
        this.txPool = txPool;
    }

    public TransactionPool getTxPool() {
        return txPool;
    }
    
}
