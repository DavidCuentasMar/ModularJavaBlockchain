package Blockchain.Model;
public class Miner {
 TransactionPool txPool;
    Miner(TransactionPool txPool) {
        this.txPool = txPool;
    }
}
