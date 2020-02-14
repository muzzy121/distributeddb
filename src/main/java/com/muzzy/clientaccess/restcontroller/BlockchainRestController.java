package com.muzzy.clientaccess.restcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muzzy.domain.Block;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.map.BlockMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;

@RestController
@RequestMapping("api/chain")
public class BlockchainRestController {
    private static final String JSON = "application/json";
    private static final Logger LOG = LoggerFactory.getLogger(SimulationController.class);

    private final BlockMapService blockMapService;
    private final TransactionOutputService transactionOutputService;

    public BlockchainRestController(BlockMapService blockMapService, TransactionOutputService transactionOutputService) {
        this.blockMapService = blockMapService;
        this.transactionOutputService = transactionOutputService;
    }

    @RequestMapping(value = "/block/lasthash", method = RequestMethod.GET)
    public String getCurrentBlock() {
        return blockMapService.getLastBlock().getHash();
    }


    @RequestMapping(value = "/block/all", method = RequestMethod.GET)
    public LinkedHashSet<Block> getAllBlocks(){
//
        ObjectMapper om = new ObjectMapper();
        TransactionOutput transaction = transactionOutputService.getAll().stream().findFirst().get();
        try {
            String test = om.writeValueAsString(transaction);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return blockMapService.getAll();
//        return null;
    }

    @RequestMapping(value = "/block/allafter/{hash}",method = RequestMethod.GET)
    public LinkedHashSet<Block> getAllFrom(@PathVariable("hash") String hash){

        return getAllBlocks();
    }

}
