package com.muzzy.clientaccess.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muzzy.domain.Block;
import com.muzzy.domain.BlockVerified;
import com.muzzy.domain.Transaction;
import com.muzzy.dto.BlockDto;
import com.muzzy.service.map.BlockMapService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BlockchainRestControllerTest {

    @Mock
    private BlockMapService blockMapService;

    @InjectMocks
    private BlockchainRestController blockchainRestController;

    private Block block;
    private LinkedHashSet<Block> blockLinkedHashSet = new LinkedHashSet<>();
    private MockMvc mockMvc;
    private MvcResult mvcResult;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        mockMvc = MockMvcBuilders.standaloneSetup(blockchainRestController).build();

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        KeyPair keyPair = keyGen.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();

        block = new BlockVerified();
        block.setHash("1");
        block.setPreviousHash("0");

        Set<Transaction> transactions = new HashSet<>();

        transactions.add(new Transaction().builder().transactionId("1").sender(publicKey).receiver(publicKey).build());
        block.setTransactions(transactions);
        blockLinkedHashSet.add(block);

    }

    @Test
    void getCurrentBlockHash() {
    }

    @Test
    void getAllBlocks() throws Exception {
        Mockito.when(blockMapService.getAll()).thenReturn(blockLinkedHashSet);
        mockMvc.perform(get("/api/chain/block/all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("[*].hash", Matchers.contains("1")));
    }

    @Test
    void getAllFrom() {
    }

    @Test
    void addBlockToChain() throws Exception {
        Mockito.when(blockMapService.getLastBlock()).thenReturn(block);

        Set<Transaction> transactions = new HashSet<>();
        transactions.add(new Transaction().builder().transactionId("1").build());
        BlockDto blockDto = new BlockDto().builder().hash("2").previousHash("1").build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = objectMapper.writeValueAsString(blockDto);

        RequestBuilder requestBuilder = post("/api/chain/block/add")
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .content(jsonObject);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(""))  //Null content
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());


    }

    @Test
    void stopMining() {
    }
}