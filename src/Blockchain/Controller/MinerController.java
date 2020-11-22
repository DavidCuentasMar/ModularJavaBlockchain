package Blockchain.Controller;

import Blockchain.Model.Block;
import Blockchain.Model.Chain;
import Blockchain.Model.Miner;
import Blockchain.Model.Transaction;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinerController {

    public static Block GenerateCandiateBock(Miner miner, Chain theChain) {
        ArrayList<Transaction> txToPublish = checkTransactions(miner.getTxPool().getTransactions(), theChain);
        if (txToPublish.size() < 1) {
            return null;
        }
        Block candidateBlock = BlockController.createNewBlock(theChain.getChainSize(),
                LocalDateTime.now().toString(),
                txToPublish,
                theChain.getLastBlock().getHash());
        miner.getTxPool().clearTxPool();
        BlockController.validate(candidateBlock, Main.Main.DIFFICULTY);
        return candidateBlock;
    }

    public static void incommingTransaction(Miner miner, Transaction transaction) {
        TransactionPoolController.addTransaction(miner.getTxPool(), transaction);
    }

    private static ArrayList<Transaction> checkTransactions(ArrayList<Transaction> transactions, Chain theChain) {
        ArrayList<Transaction> txToPublish = new ArrayList<>();
        Iterator txIterator = transactions.iterator();
        while (txIterator.hasNext()) {
            Transaction tx = (Transaction) txIterator.next();
            System.out.println(tx.getData()[0]);
            //System.out.println("CurrentTx: " + tx.getData()[0]);
            boolean removeTx = false;
            String contractName = tx.getTo_address();
            if (TransactionController.checkTransactionSignature(tx) == false) {
                removeTx = true;
            }
            if (removeTx == false) {
                //System.out.println(contractName);
                try {
                    Class<?> smartContractClass = Class.forName("Blockchain.Contracts." + contractName);
                    Object smartContractObj = smartContractClass.newInstance();
                    Method m = smartContractObj.getClass().getMethod("run", Chain.class, Transaction.class);
                    boolean contractSuccess = (boolean) m.invoke(smartContractObj, theChain, tx);
                    if (contractSuccess == false) {
                        removeTx = true;
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MinerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(MinerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(MinerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(MinerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(MinerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(MinerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(MinerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (removeTx) {
                txIterator.remove();
            } else {
                txToPublish.add(tx);
            }
        }
        return txToPublish;
    }
}
