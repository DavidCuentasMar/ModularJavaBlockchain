package Utils;

import Blockchain.Model.Transaction;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class DigitalSignature {

    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";

    public static void sign(Transaction tx, String privateKeyString) {
        try {
            EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            Signature ecdsaSign = Signature.getInstance(ALGO);
            ecdsaSign.initSign(privateKey);
            ecdsaSign.update(tx.getHash().getBytes("UTF-8"));
            byte[] signature = ecdsaSign.sign();
            String sig = Base64.getEncoder().encodeToString(signature);
            tx.setSignature(sig);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(DigitalSignature.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JSONObject sender() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException, SignatureException {

        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        KeyPair keypair = g.generateKeyPair();
        PublicKey publicKey = keypair.getPublic();
        PrivateKey privateKey = keypair.getPrivate();

        String plaintext = "Hello";

        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(plaintext.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String sig = Base64.getEncoder().encodeToString(signature);

        JSONObject obj = new JSONObject();
        obj.put("publicKey", pub);
        obj.put("signature", sig);
        obj.put("message", plaintext);
        obj.put("algorithm", ALGO);

        return obj;
    }

    public boolean receiver(JSONObject obj) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException {

        Signature ecdsaVerify = Signature.getInstance(obj.getString("algorithm"));
        KeyFactory kf = KeyFactory.getInstance("EC");

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(obj.getString("publicKey")));

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(obj.getString("message").getBytes("UTF-8"));
        boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(obj.getString("signature")));

        return result;
    }

    public static KeyPair generateKeyPair() {
        try {
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
            KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
            g.initialize(ecSpec, new SecureRandom());
            return g.generateKeyPair();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public static void generateKeys() throws Exception {
        // https://stackoverflow.com/questions/11339788/tutorial-of-ecdsa-algorithm-to-sign-a-string
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(new ECGenParameterSpec("secp256k1"), new SecureRandom());
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        PublicKey pub = pair.getPublic();
        /*
         * Create a Signature object and initialize it with the private key
         */

        Signature ecdsa = Signature.getInstance("SHA256withECDSA");

        ecdsa.initSign(priv);

        String str = "This is string to sign";
        byte[] strByte = str.getBytes("UTF-8");
        ecdsa.update(strByte);

        /*
         * Now that all the data to be signed has been read in, generate a
         * signature for it
         */
        byte[] realSig = ecdsa.sign();
        System.out.println("Signature: " + new BigInteger(1, realSig).toString(16));

        ecdsa.verify(strByte);
    }

    public static String firmaTx(Transaction tx, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException, InvalidKeySpecException {
        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(tx.getHash().getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        String sig = Base64.getEncoder().encodeToString(signature);
        return sig;
    }

    public static boolean checkSign(Transaction tx) {
        boolean result = false;
        try {
            Signature ecdsaVerify = Signature.getInstance(ALGO);
            KeyFactory kf = KeyFactory.getInstance("EC");

            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(tx.getFrom_address()));

            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(tx.getHash().getBytes("UTF-8"));
            if (tx.getSignature() != null) {
                result = ecdsaVerify.verify(Base64.getDecoder().decode(tx.getSignature()));
            }
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }
}
