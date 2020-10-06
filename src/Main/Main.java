package Main;

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
        miner1.ReceiveTransaction(new Transaction("addrx1","addrx2",new String[]{"A"}));
        miner1.ReceiveTransaction(new Transaction("addrx2","addrx3",new String[]{"B"}));
        miner1.ReceiveTransaction(new Transaction("addrx3","addrx4",new String[]{"C"}));

        //Miner Generates Candidate Block
        Block minerBlock = miner1.GenerateCandiateBock();
        miner1.broadCastBlock(minerBlock);
        theChain.addBlock(minerBlock);
        
        Miner miner2 = new Miner(new TransactionPool());
        miner2.ReceiveTransaction(new Transaction("addrx8","addrx9",new String[]{"E"}));
        miner2.ReceiveTransaction(new Transaction("addrx9","addrx10",new String[]{"F"}));
        miner2.ReceiveTransaction(new Transaction("addrx10","addrx11",new String[]{"G"}));

        //Miner Generates Candidate Block
        Block minerBlock2 = miner2.GenerateCandiateBock();
        miner2.broadCastBlock(minerBlock2);
        theChain.addBlock(minerBlock2);
        
        theChain.listAllBlocks();
    }
}
