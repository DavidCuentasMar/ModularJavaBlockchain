package Blockchain.Model;
public class Miner {
    public TransactionPool txPool;
    public Miner(TransactionPool txPool) {
        this.txPool = txPool;
    }
}
