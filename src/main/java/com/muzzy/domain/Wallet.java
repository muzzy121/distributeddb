package com.muzzy.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Getter
@Setter
@Component
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet(){
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(512);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: 2020-01-27 Czy nie lepiej by było aby wyciąganie string-a z PubKey-a było tutaj? Czy jest to nam potrzebne gdzie indziej?
    public String getStringFromPubKey() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    public String getStringFromPrvKey() {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "publicKey=" + publicKey +
                '}';
    }

    /**
     * Moved to TransactionOutputService
     *
     */
//    public float getBalance() {
//    }

    /**
     * Moved to TransactionFactory
     */
//    public Transaction sendFunds(PublicKey receiver, float value) {

//    }
}