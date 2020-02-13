package com.muzzy.clientaccess.controller;

import com.muzzy.domain.TransactionOutput;
import com.muzzy.domain.Wallet;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.map.WalletMapService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Pawel Mazur on 30.01.2020
 */
@ExtendWith(MockitoExtension.class)
class WalletControllerTest {
    @Mock
    WalletMapService walletMapService;

    @Mock
    TransactionOutputService transactionOutputService;

    @InjectMocks
    WalletController walletController;

    MockMvc mockMvc;
    Wallet wallet;
    TransactionOutput transactionOutput;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        transactionOutput = new TransactionOutput();
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    void getWallets() throws Exception {
        Set<Wallet> wallets = new HashSet<>();
        wallets.add(wallet);
        when(walletMapService.getAll()).thenReturn(wallets);

        mockMvc.perform(get("/wallets"))
                .andExpect(status().isOk())
                .andExpect(view().name("wallet/index"))
                .andExpect(model().attribute("wallets", hasSize(1)));
        verify(walletMapService).getAll();
    }

    @Test
    void getWalletDetail() throws Exception {
        when(walletMapService.getById(anyString())).thenReturn(wallet);
        when(transactionOutputService.getBalance(any())).thenReturn(BigDecimal.valueOf(10f));
        mockMvc.perform(post("/wallets/detail").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("wallet/detail"))
                .andExpect(model().attribute("wallet", Matchers.any(Wallet.class)))
                .andExpect(model().attribute("balance", is(10f)));
        verify(walletMapService).getById(anyString());
        verify(transactionOutputService).getBalance(any());
    }
}