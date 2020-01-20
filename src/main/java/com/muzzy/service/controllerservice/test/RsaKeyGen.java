package com.muzzy.service.controllerservice.test;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class RsaKeyGen {

    private KeyPairGenerator rsaKeyGen;
    private KeyPair pair;
    @Getter
    private PrivateKey prvKey;
    @Getter
    private PublicKey pubKey;

    public RsaKeyGen() throws NoSuchAlgorithmException {
        this.rsaKeyGen = KeyPairGenerator.getInstance("RSA");
        this.rsaKeyGen.initialize(1024);
    }

    public void createKeys() {
        this.pair = this.rsaKeyGen.generateKeyPair();
        this.prvKey = pair.getPrivate();
        this.pubKey = pair.getPublic();
    }


}
