package com.muzzy.service.map;

import com.muzzy.domain.Block;
import com.muzzy.domain.BlockVerified;
import com.muzzy.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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
//        ArrayList<Transaction> transactions = new ArrayList<>();
        Set<Transaction> transactions = new HashSet<>();

        transactions.add(new Transaction().builder().transactionId("1").build());
        block.setTransactions(transactions);
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
        assertNotNull(blockMapService.getTransactionFromBlockById("1"));
        assertEquals("1", blockMapService.getTransactionFromBlockById("1").getTransactionId());
    }

    @Test
    void getBlockWithTransaction() {
        assertNotNull(blockMapService.getBlockWithTransaction("1"));
        assertEquals("1",blockMapService.getBlockWithTransaction("1").getHash());
    }
}