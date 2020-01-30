package com.muzzy.service.map;

import com.muzzy.domain.Wallet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WalletMapServiceTest {
    WalletMapService walletMapService;
    PrivateKey privateKey;
    PublicKey publicKey;
    String stringFormatPublicKey;


    @BeforeEach
    void setUp() {
        walletMapService = new WalletMapService();
        Wallet wallet = new Wallet();
        walletMapService.save(wallet);
        privateKey = wallet.getPrivateKey();
        publicKey = wallet.getPublicKey();
        this.stringFormatPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    @Test
    void getAll() {
        Set<Wallet> all = walletMapService.getAll();
        assertEquals(1,all.size());
    }

    @Test
    void deleteById() {
        walletMapService.deleteById(stringFormatPublicKey);
        Set<Wallet> all = walletMapService.getAll();
        assertEquals(0, all.size());
    }

    @Test
    void delete() {
        walletMapService.delete(walletMapService.findById(stringFormatPublicKey));
        Set<Wallet> all = walletMapService.getAll();
    }

    @Test
    void save() {
        walletMapService.save(new Wallet());
        assertEquals(2, walletMapService.getAll().size());
    }

    @Test
    void getById() {
        Wallet result = walletMapService.getById(stringFormatPublicKey);
        assertEquals(result.getPublicKey(),publicKey);
    }
}