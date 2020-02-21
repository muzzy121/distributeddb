package com.muzzy.service.controllerservice;

import com.muzzy.domain.AncestorTransaction;
import com.muzzy.domain.Block;
import com.muzzy.domain.BlockVerified;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {
    private Block block;
    private Block genesisBlock;
    // Mock blockService


    @BeforeEach
    void setUp() {
        genesisBlock = new BlockVerified("0");
        genesisBlock.addTransaction(new AncestorTransaction("1","2",new BigDecimal(100),null, null, "0"));
        genesisBlock.mine(4);
        block = new BlockVerified(genesisBlock.getHash());
        block.setTimestamp(ZonedDateTime.now());
    }

    @Test
    void confirm() {
    }

    @Test
    void verifySignature() {
    }

    @Test
    void calculateHash() {
        block.mine(5);
        String hash = Validation.calculateHash(block);
        assertEquals(block.getHash(), hash);
    }

    @Test
    void isChainValid() {
        block.mine();
    }

    @Test
    void calculateDifficulty() {
        int whenDifficulty = 5;
        block.mine(whenDifficulty);
        Integer difficulty = Validation.calculateDifficulty(block);
        assertEquals(whenDifficulty,difficulty);
    }
}