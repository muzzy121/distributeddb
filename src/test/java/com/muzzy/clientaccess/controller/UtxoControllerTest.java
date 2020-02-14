package com.muzzy.clientaccess.controller;

import com.muzzy.domain.TransactionOutput;
import com.muzzy.service.TransactionOutputService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Pawel Mazur on 30.01.2020
 *
 */
@ExtendWith(MockitoExtension.class)
class UtxoControllerTest {
    @Mock
    TransactionOutputService transactionOutputService;

    @InjectMocks
    UtxoController utxoController;

    Set<TransactionOutput> transactionOutputs;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        transactionOutputs = new HashSet<>();
        transactionOutputs.add(new TransactionOutput("10",null, BigDecimal.valueOf(10F),"0"));
        transactionOutputs.add(new TransactionOutput("20",null, BigDecimal.valueOf(20F),"1"));
        mockMvc = MockMvcBuilders.standaloneSetup(utxoController).build();
    }


    @Test
    void getIndexPage() throws Exception {
        when(transactionOutputService.getAll()).thenReturn(transactionOutputs);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/index"))
                .andExpect(model().attribute("transactions", hasSize(2)));

        verify(transactionOutputService,times(1)).getAll();
    }
}