package Blockchain.Controller;

import Blockchain.Model.Transaction;
import Utils.DigitalSignature;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WalletController {

    public static void signTransaction(Transaction tx, PrivateKey privateKey) {
        try {
            String signatureTx = DigitalSignature.firmaTx(tx, privateKey);
            tx.setSignature(signatureTx);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static Transaction generateTx(String publicKeyStr, String smartContractName, String txData) {
        return new Transaction(publicKeyStr, smartContractName, new String[]{publicKeyStr,txData});
    }

}
