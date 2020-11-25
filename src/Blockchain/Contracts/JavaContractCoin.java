package Blockchain.Contracts;

import Blockchain.Model.Block;
import Blockchain.Model.Chain;
import Blockchain.Model.Transaction;
import java.util.ArrayList;

public class JavaContractCoin extends JavaContract {

    public boolean run(Chain theChain, Transaction tx, ArrayList<Transaction> correctUnpublishedTxs) {
        /*System.out.println("[Start - Random Current Tx]");
        System.out.println("    PublicKey: " + tx.from_address);
        System.out.println("    SmartContract:" + tx.to_address);
        System.out.println("    PubliKey Destiny: " + tx.getData()[0]);
        System.out.println("    Amount: " + tx.getData()[1]);
        System.out.println("[End - Random Current Tx]");        */
        boolean contractSuccess = false;
        double amountToSend = Double.valueOf(tx.getData()[1]);
        double totalInput = 0.0;
        double totalOutput = 0.0;
        if (correctUnpublishedTxs.size() > 0) {
            for (Transaction currentCorrectUnpublishedTx : correctUnpublishedTxs) {
                if ((currentCorrectUnpublishedTx.getFrom_address()).equals(tx.getFrom_address())) {
                    totalOutput = totalOutput + Double.valueOf(currentCorrectUnpublishedTx.getData()[1]);
                }
                if ((currentCorrectUnpublishedTx.getData()[0]).equals(tx.getFrom_address())) {
                    totalInput = totalInput + Double.valueOf(currentCorrectUnpublishedTx.getData()[1]);
                }
            }
        }
        ArrayList<Transaction> targetTransactions = new ArrayList<>();
        for (Block currentBlock : theChain.getChain()) {
            if (currentBlock.getIndex() == 0) {
                //System.out.println("[Checking Genesis] " + theChain.getChainSize());
                for (Transaction currentBlockTx : currentBlock.getTransactions()) {
                    if ((currentBlockTx.getTo_address()).equals("JavaContractCoin")) {
                        /*System.out.println("----");
                        System.out.println(currentBlockTx.getData()[0]);
                        System.out.println(tx.getFrom_address());
                        System.out.println("----");
                        System.out.println(totalInput);
                        System.out.println(Double.valueOf(currentBlockTx.getData()[1]));
                        System.out.println(totalInput + Double.valueOf(currentBlockTx.getData()[1]));*/
                        if ((currentBlockTx.getData()[0]).equals(tx.getFrom_address())) {
                            totalInput = totalInput + Double.valueOf(currentBlockTx.getData()[1]);
                        }
                    }
                }
            } else {
                //System.out.println("{-----NO GENESIS - BLOCK # " + currentBlock.getIndex() + "}----");
                //System.out.println(currentBlock.getTransactions().size());
                for (Transaction currentBlockTx : currentBlock.getTransactions()) {
                    //System.out.println("SmartConract: " + currentBlockTx.getTo_address());
                    if ((currentBlockTx.getTo_address()).equals("JavaContractCoin")) {
                        //System.out.println("ORIGIN: " + currentBlockTx.getFrom_address());
                        //System.out.println("DESTINY: " + tx.getFrom_address());
                        if ((currentBlockTx.getData()[0]).equals(tx.getFrom_address())) {
                            totalInput = totalInput + Double.valueOf(currentBlockTx.getData()[1]);
                        }
                        if ((currentBlockTx.getFrom_address()).equals(tx.getFrom_address())) {
                            totalOutput = totalOutput + Double.valueOf(currentBlockTx.getData()[1]);
                        }
                    }
                }
                //System.out.println("{-----END NO GENESIS}----");

            }
        }
        if ((totalInput - totalOutput) >= amountToSend) {
            contractSuccess = true;
        }
        /*System.out.println("totalInput: " + totalInput);
        System.out.println("totalOutput: " + totalOutput);
        System.out.println("amountToSend: " + amountToSend);
        System.out.println("################");
        System.out.println(correctUnpublishedTxs.size());
        System.out.println("################");*/
        return contractSuccess;
    }

    @Override
    void run() {
        System.out.println("EMPTY CONSTRUCTOR");
    }

}
