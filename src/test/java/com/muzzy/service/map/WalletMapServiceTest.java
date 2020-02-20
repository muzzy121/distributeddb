package com.muzzy.service.map;

import com.muzzy.domain.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WalletMapServiceTest {
    WalletMapService walletMapService;
    String privateKey;
    String publicKey;


    @BeforeEach
    void setUp() {
        walletMapService = new WalletMapService();
        Wallet wallet = new Wallet();
        walletMapService.save(wallet);
        privateKey = wallet.getPrivateKey();
        publicKey = wallet.getPublicKey();
    }

    @Test
    void getAll() {
        Set<Wallet> all = walletMapService.getAll();
        assertEquals(1,all.size());
    }

    @Test
    void deleteById() {
        walletMapService.deleteById(publicKey);
        Set<Wallet> all = walletMapService.getAll();
        assertEquals(0, all.size());
    }

    @Test
    void delete() {
        walletMapService.delete(walletMapService.findById(publicKey));
        Set<Wallet> all = walletMapService.getAll();
    }

    @Test
    void save() {
        walletMapService.save(new Wallet());
        assertEquals(2, walletMapService.getAll().size());
    }

    @Test
    void getById() {
        Wallet result = walletMapService.getById(publicKey);
        assertEquals(result.getPublicKey(),publicKey);
    }
}