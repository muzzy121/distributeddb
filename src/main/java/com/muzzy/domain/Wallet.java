package com.muzzy.domain;

import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionInput;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.service.TransactionOutputService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.*;
import java.util.stream.Collectors;

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

    // TODO: 2020-01-27 Czy nie lepiej by było aby wiciąganie string-a z PubKey-a było tutaj? Czy jest to nam potrzebne gdzie indziej?
    public String getStringFromPubKey() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    public String getStringFromPrvKey() {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
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