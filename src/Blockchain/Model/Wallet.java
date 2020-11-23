package Blockchain.Model;

import Utils.DigitalSignature;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Wallet {
    public PublicKey publicKey;
    private PrivateKey privateKey;
    private KeyPair keyPair;
    public Wallet(){
        this.keyPair = DigitalSignature.generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }
    public String getPublicKeyStr(){
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    public KeyPair getKeyPair() {
        return keyPair;
    }
}
