package com.muzzy.service.map;

import com.muzzy.domain.Block;
import com.muzzy.domain.BlockVerified;
import com.muzzy.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

class BlockMapServiceTest {

    private BlockMapService blockMapService;
    private Block block;

    @BeforeEach
    void setUp() {
        blockMapService = new BlockMapService();
        block = new BlockVerified();
        block.setHash("1");
        block.setPreviousHash("0");

        LinkedHashSet<Transaction> transactions = new LinkedHashSet<>();
        transactions.add(new Transaction().builder().transactionId("1").build());
        block.setTransactions(transactions);
        blockMapService.save(block);
    }

    @Test
    void getAll() {
        LinkedHashSet<Block> all = blockMapService.getAll();
        assertEquals(1, all.size());
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
    void whenSave_givenNullTransaction_ReturnNotAdded() {
        Block newBlock = new BlockVerified();
        newBlock.setHash("2");
        newBlock.setPreviousHash("1");
        blockMapService.save(newBlock);
        assertEquals(1, blockMapService.getAll().size());
        assertNull(blockMapService.getById("2"));
    }

    @Test
    void whenSave_givenTransaction_returnAdded() {
        Block newBlock = new BlockVerified();
        newBlock.setHash("2");
        newBlock.setPreviousHash("1");
        LinkedHashSet<Transaction> transactions = new LinkedHashSet<>();
        transactions.add(new Transaction().builder().transactionId("1").build());
        newBlock.setTransactions(transactions);
        blockMapService.save(newBlock);
        assertEquals(2, blockMapService.getAll().size());
        assertEquals(newBlock.getHash(), blockMapService.getById("2").getHash());
    }

    @Test
    void getById() {
        assertEquals("1", blockMapService.getById("1").getHash());
    }

    @Test
    void whenAddBlock_getLastBlock_returnLast() {
        Block newBlock = new BlockVerified();
        newBlock.setHash("2");
        newBlock.setPreviousHash("1");
        LinkedHashSet<Transaction> transactions = new LinkedHashSet<>();
        transactions.add(new Transaction().builder().transactionId("1").build());
        newBlock.setTransactions(transactions);
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
        assertEquals("1", blockMapService.getBlockWithTransaction("1").getHash());
    }

    @Test
    void givenAllBlocks_whenGetBlocksAfter_thenGetLastFromTheOne() {
        //Add to new blocks
        Block newBlock = new BlockVerified();
        newBlock.setHash("2");
        newBlock.setPreviousHash("1");
        LinkedHashSet<Transaction> transactions = new LinkedHashSet<>();
        transactions.add(new Transaction().builder().transactionId("1").build());
        newBlock.setTransactions(transactions);
        blockMapService.save(newBlock);

        Block newBlock2 = new BlockVerified();
        newBlock.setHash("3");
        newBlock.setPreviousHash("2");
        LinkedHashSet<Transaction> transactions2 = new LinkedHashSet<>();
        transactions2.add(new Transaction().builder().transactionId("2").build());
        newBlock2.setTransactions(transactions2);
        blockMapService.save(newBlock2);

        //Should return only one element
        assertEquals(1, blockMapService.getBlocksAfter(newBlock.getHash()).size());
        //Should return two elements
        assertEquals(2, blockMapService.getBlocksAfter("1").size());
    }
}