package com.muzzy.service.controllerservice;

import com.muzzy.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationTest {
    private Block block;
    private Block genesisBlock;
    private Transaction transaction;
    private Wallet wallet;


    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        genesisBlock = new BlockVerified("0");

        AncestorTransaction ancestorTransaction = new AncestorTransaction(wallet.getPublicKey(), "3333", new BigDecimal(100), null, null, "0");
        String s = ancestorTransaction.getSender() + ancestorTransaction.getReceiver() + ancestorTransaction.getValue();
        ancestorTransaction.setSignature(Validation.confirm(wallet.getPrivateKey(), s));

        genesisBlock.addTransaction(ancestorTransaction);
        genesisBlock.mine(4);
        block = new BlockVerified(genesisBlock.getHash());
        block.setTimestamp(ZonedDateTime.now());
        block.mine(5);

        transaction = new Transaction().builder()
                .sender(wallet.getPublicKey())
                .receiver("2222")
                .value(new BigDecimal(100))
                .build();
    }

    @Test
    void verifySignature() {
        String data = transaction.getSender() + transaction.getReceiver() + transaction.getValue();
        byte[] signature = Validation.confirm(wallet.getPrivateKey(), data);

        boolean test = Validation.verifySignature(wallet.getPublicKey(), data, signature);

        //Test if data OK
        assertTrue(test);

        String data2 = transaction.getSender() + transaction.getReceiver() + transaction.getValue() + "1";
        boolean test2 = Validation.verifySignature(wallet.getPublicKey(), data, signature);

        //Test if data are not OK
        assertTrue(test2);
    }

    @Test
    void calculateHash() {
        String hash = Validation.calculateHash(block);
        assertEquals(block.getHash(), hash);
    }

    @Test
    void isChainValid() {

        List<Block> blockLinkedHashSet = new ArrayList<>();
        blockLinkedHashSet.add(genesisBlock);
        blockLinkedHashSet.add(block);
        assertTrue(Validation.isChainValid(blockLinkedHashSet));
    }

    @Test
    void calculateDifficulty() {
        int whenDifficulty = 5;
        block.mine(whenDifficulty);
        Integer difficulty = Validation.calculateDifficulty(block);
        assertEquals(whenDifficulty, difficulty);
    }
}