package Main;

import Blockchain.Controller.MinerController;
import Blockchain.Model.Block;
import Blockchain.Model.Chain;
import Blockchain.Model.Miner;
import Blockchain.Model.Transaction;
import Blockchain.Model.TransactionPool;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static final int DIFFICULTY = 3;
    public static void main(String[] args) {
        //Chain creation
        Chain theChain = new Chain();
        
        Miner miner1 = new Miner(new TransactionPool());
        Miner miner2 = new Miner(new TransactionPool());

        
        MinerController.incommingTransaction(miner1, new Transaction("addrx1","addrx2",new String[]{"A"}));
        MinerController.incommingTransaction(miner1, new Transaction("addrx2","addrx3",new String[]{"B"}));
        MinerController.incommingTransaction(miner1, new Transaction("addrx3","addrx4",new String[]{"C"}));

        //Miner Generates Candidate Block
        Block minerBlock = MinerController.GenerateCandiateBock(miner1, theChain.getChainSize(), theChain.getLastBlock().getHash());
        System.out.println(minerBlock);
        MinerController.broadCastBlock(miner1, minerBlock);
        theChain.addBlock(minerBlock);
        
        MinerController.incommingTransaction(miner2, new Transaction("addrx8","addrx9",new String[]{"E"}));
        MinerController.incommingTransaction(miner2, new Transaction("addrx9","addrx10",new String[]{"F"}));
        MinerController.incommingTransaction(miner2, new Transaction("addrx10","addrx11",new String[]{"G"}));

        //Miner Generates Candidate Block
        Block minerBlock2 = MinerController.GenerateCandiateBock(miner2, theChain.getChainSize(), theChain.getLastBlock().getHash());
        MinerController.broadCastBlock(miner2, minerBlock2);
        theChain.addBlock(minerBlock2);
        
        theChain.listAllBlocks();
    }
}
