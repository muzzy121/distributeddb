package com.muzzy.clientaccess.restcontroller;

import com.muzzy.domain.Block;
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

    public BlockchainRestController(BlockMapService blockMapService) {
        this.blockMapService = blockMapService;
    }

    @RequestMapping(value = "/block/lasthash", method = RequestMethod.GET)
    public String getCurrentBlock() {
        return blockMapService.getLastBlock().getHash();
    }
    @RequestMapping(value = "/block/all", method = RequestMethod.GET)
    public LinkedHashSet<Block> getAllBlocks(){
        return blockMapService.getAll();
    }
    @RequestMapping(value = "/block/allafter/{hash}",method = RequestMethod.GET)
    public LinkedHashSet<Block> getAllFrom(@PathVariable("hash") String hash){

        return getAllBlocks();
    }

}
