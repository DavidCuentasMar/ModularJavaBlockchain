package Blockchain.Contracts;

import Blockchain.Model.Block;
import Blockchain.Model.Chain;
import Blockchain.Model.Transaction;
import java.util.ArrayList;

public class JavaContractCoin extends JavaContract {

    public boolean run(Chain theChain, Transaction tx, ArrayList<Transaction> txsToValidate) {
        System.out.println("################");
        System.out.println(txsToValidate.size());
        System.out.println("################");
        boolean contractSuccess = false;
        double amountToSend = Double.valueOf(tx.getData()[1]);
        double totalInput = 0;
        double totalOutput = 0;
        ArrayList<Transaction> targetTransactions = new ArrayList<>();
        for (Block currentBlock : theChain.getChain()) {
//            System.out.println(currentBlock.getIndex());
//            System.out.println(currentBlock.getTransactions().get(0).getFrom_address());
//            System.out.println(currentBlock.getTransactions().get(0).getTo_address());
//            System.out.println(currentBlock.getTransactions().get(0).getData()[0]);
//            System.out.println(currentBlock.getTransactions().get(0).getData()[1]);

            for (Transaction currentBlockTx : currentBlock.getTransactions()) {
                System.out.println("currentBlockTx.getTo_address()");
                System.out.println(currentBlockTx.getTo_address());
                if (currentBlockTx.getTo_address() == "JavaContractCoin") {
                    if (currentBlock.getIndex() == 0) {
                        if (currentBlockTx.getData()[0] == tx.getFrom_address()) {
                            totalInput += Double.valueOf(currentBlockTx.getData()[1]);
                        }
                    } else {
                        if (currentBlockTx.getFrom_address() == tx.getFrom_address()) {
                            totalOutput += Double.valueOf(currentBlockTx.getData()[1]);
                        }
                        if (currentBlockTx.getData()[0] == tx.getFrom_address()) {
                            totalInput += Double.valueOf(currentBlockTx.getData()[1]);
                        }
                    }
                }
            }
        }
        if ((totalInput - totalOutput) >= amountToSend) {
            contractSuccess = true;
        }
        System.out.println(totalInput);
        System.out.println(totalOutput);
        System.out.println("*****");
        return contractSuccess;
    }

    @Override
    void run() {
        System.out.println("EMPTY CONSTRUCTOR");
    }

}
