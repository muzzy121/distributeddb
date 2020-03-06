package com.muzzy.clientaccess.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.Block;
import com.muzzy.domain.BlockVerified;
import com.muzzy.domain.Transaction;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.map.BlockMapService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BlockchainRestControllerTest {

    @Mock
    private BlockMapService blockMapService;
    @Mock
    private TransactionOutputService transactionOutputService;

    @InjectMocks
    private BlockchainRestController blockchainRestController;

    private Block block;
    private LinkedHashSet<Block> blockLinkedHashSet = new LinkedHashSet<>();
    private MockMvc mockMvc;
    private MvcResult mvcResult;
    private String privateKey;
    private String publicKey;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        mockMvc = MockMvcBuilders.standaloneSetup(blockchainRestController).build();

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        KeyPair keyPair = keyGen.generateKeyPair();
        privateKey = StringUtil.getStringFromKey(keyPair.getPrivate());
        publicKey = StringUtil.getStringFromKey(keyPair.getPublic());


        block = new BlockVerified();
        block.setHash("1");
        block.setPreviousHash("0");
        block.setTimestamp(ZonedDateTime.now());

        LinkedHashSet<Transaction> transactions = new LinkedHashSet<>();

        transactions.add(new Transaction().builder().transactionId("1").sender(publicKey).receiver(publicKey).build());
        block.setTransactions(transactions);
        blockLinkedHashSet.add(block);

    }

    @Test
    void getCurrentBlockHash() {
    }

    @Test
    void getAllBlocks() throws Exception {
        when(blockMapService.getAll()).thenReturn(blockLinkedHashSet);
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
        when(blockMapService.getLastBlock()).thenReturn(block);

        Set<Transaction> transactions = new HashSet<>();
        transactions.add(new Transaction().builder().transactionId("1").build());
        Block blockDto = new BlockVerified("1");
        blockDto.setTimestamp(ZonedDateTime.now());
        blockDto.mine(2);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss z"));

        String jsonObject = objectMapper.writeValueAsString(blockDto);
        System.out.println(jsonObject);

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