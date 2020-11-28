package Blockchain.Contracts;

import Blockchain.Model.Block;
import Blockchain.Model.Chain;
import Blockchain.Model.Transaction;
import java.util.ArrayList;

public class JavaContractCoin extends JavaContract {

    public boolean run(Chain theChain, Transaction tx, ArrayList<Transaction> correctUnpublishedTxs) {
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
                for (Transaction currentBlockTx : currentBlock.getTransactions()) {
                    if ((currentBlockTx.getTo_address()).equals("JavaContractCoin")) {
                        if ((currentBlockTx.getData()[0]).equals(tx.getFrom_address())) {
                            totalInput = totalInput + Double.valueOf(currentBlockTx.getData()[1]);
                        }
                    }
                }
            } else {
                for (Transaction currentBlockTx : currentBlock.getTransactions()) {
                    if ((currentBlockTx.getTo_address()).equals("JavaContractCoin")) {
                        if ((currentBlockTx.getData()[0]).equals(tx.getFrom_address())) {
                            totalInput = totalInput + Double.valueOf(currentBlockTx.getData()[1]);
                        }
                        if ((currentBlockTx.getFrom_address()).equals(tx.getFrom_address())) {
                            totalOutput = totalOutput + Double.valueOf(currentBlockTx.getData()[1]);
                        }
                    }
                }
            }
        }
        if ((totalInput - totalOutput) >= amountToSend) {
            contractSuccess = true;
        }
        return contractSuccess;
    }

    @Override
    void run() {
        System.out.println("EMPTY CONSTRUCTOR");
    }

}
