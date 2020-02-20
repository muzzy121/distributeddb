package com.muzzy.service.map;

import com.muzzy.domain.TransactionOutput;
import com.muzzy.domain.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionOutputMapServiceTest {
    TransactionOutputMapService transactionOutputMapService;
    TransactionOutput transactionOutput;
    String mockPublicKey;

    @BeforeEach
    void setUp() {
        Wallet wallet = new Wallet();
        transactionOutputMapService = new TransactionOutputMapService();
        transactionOutput = new TransactionOutput();
        transactionOutput.setId("1");
        transactionOutput.setValue(BigDecimal.valueOf(10));
        transactionOutput.setReceiver(wallet.getPublicKey());
        transactionOutputMapService.save(transactionOutput);
        // ---- For getTransctionByReciever and getBalance
//        Mockito.when(transactionOutputMapService.getTransctionByReciever(Mockito.any(PublicKey.class)))
//                .thenReturn(transactionOutputMapService.getAll());

        // ---- For getBalance
        mockPublicKey = Mockito.anyString();
    }

    @Test
    void getAll() {
        assertEquals(1, transactionOutputMapService.getAll().size());
    }

    @Test
    void getById() {
        assertEquals("1",transactionOutputMapService.getById("1").getId());
    }

    @Test
    void save() {
        TransactionOutput newTransactionOutput = new TransactionOutput();
        newTransactionOutput.setId("2");
        transactionOutputMapService.save(newTransactionOutput);
        assertEquals(2,transactionOutputMapService.getAll().size());
    }
    @Test
    void delete() {
        transactionOutputMapService.delete(transactionOutput);
        assertEquals(0,transactionOutputMapService.getAll().size());
    }

    @Test
    void deleteById() {
        transactionOutputMapService.deleteById(transactionOutput.getId());
        assertEquals(0,transactionOutputMapService.getAll().size());
    }

    @Test
    void getTransctionByReciever() {
        Set<TransactionOutput> transactionOutputs = transactionOutputMapService.getTransctionByReciever(mockPublicKey);
        assertEquals(0, transactionOutputs.size());
    }

    @Test
    void getBalance() {
        BigDecimal balance = transactionOutputMapService.getBalance(mockPublicKey);
        assertEquals(BigDecimal.valueOf(10F),balance);
    }
}