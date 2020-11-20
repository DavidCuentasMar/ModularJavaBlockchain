package Blockchain.Controller;

import Blockchain.Model.Transaction;
import Utils.DigitalSignature;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionController {

    public static void signTransaction(Transaction tx, PublicKey publicKey, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            String signatureTx = DigitalSignature.firmaTx(tx, publicKey, privateKey);
            tx.setSignature(signatureTx);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(TransactionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void checkTransaction(Transaction tx, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException, UnsupportedEncodingException {
        System.out.println(DigitalSignature.checkSign(tx, publicKey));
    }
}
