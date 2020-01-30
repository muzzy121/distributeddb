package com.muzzy.service.map;

import com.muzzy.domain.Block;
import com.muzzy.domain.BlockVerified;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

class BlockMapServiceTest {
    BlockMapService blockMapService;
    Block block;

    @BeforeEach
    void setUp() {
        blockMapService = new BlockMapService();
        block = new BlockVerified();
        block.setHash("1");
        block.setPreviousHash("0");
        blockMapService.save(block);
    }

    @Test
    void getAll() {
        LinkedHashSet<Block> all = blockMapService.getAll();
        assertEquals(1,all.size());
    }

    @Test
    void deleteById() {
        blockMapService.deleteById("1");
        assertEquals(0, blockMapService.getAll().size());
    }

    @Test
    void delete() {
        blockMapService.delete(block);
        assertEquals(0, blockMapService.getAll().size());
    }

    @Test
    void save() {
        Block newBlock = new BlockVerified();
        newBlock.setHash("2");
        newBlock.setPreviousHash("1");
        blockMapService.save(newBlock);
        assertEquals(2, blockMapService.getAll().size());
        assertEquals(newBlock.getHash(), blockMapService.getById("2").getHash());
    }

    @Test
    void getById() {
        assertEquals("1", blockMapService.getById("1").getHash());
    }

    @Test
    void getLastBlock() {
        Block newBlock = new BlockVerified();
        newBlock.setHash("2");
        newBlock.setPreviousHash("1");
        blockMapService.save(newBlock);
        assertEquals(newBlock, blockMapService.getLastBlock());
    }

    @Test
    void getTransactionFromBlockById() {
    }

    @Test
    void getBlockWithTransaction() {
    }
}