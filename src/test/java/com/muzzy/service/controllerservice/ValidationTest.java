package com.muzzy.service.controllerservice;

import com.muzzy.domain.Block;
import com.muzzy.domain.BlockVerified;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {
    private Block block;


    @BeforeEach
    void setUp() {
        block = new BlockVerified("1111");
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
    }
}