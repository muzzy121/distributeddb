package com.muzzy.clientaccess.restcontroller;

import com.muzzy.configuration.ConfigLoader;
import com.muzzy.configuration.RestApiConfig;
import com.muzzy.domain.Block;
import com.muzzy.domain.BlockVerified;
import com.muzzy.net.commands.StopMsg;
import com.muzzy.roles.Miner;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.controllerservice.Validation;
import com.muzzy.service.map.BlockMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;

@RestController
@RequestMapping("api/chain")
public class BlockchainRestController {
    private static final String JSON = "application/json";
    private static final Logger LOG = LoggerFactory.getLogger(SimulationController.class);

    private final ConfigLoader configLoader;
    private final BlockMapService blockMapService;
    private final TransactionOutputService transactionOutputService;
    private RestApiConfig restApiConfig;

    public BlockchainRestController(ConfigLoader configLoader, BlockMapService blockMapService, TransactionOutputService transactionOutputService) {
        this.configLoader = configLoader;
        this.blockMapService = blockMapService;
        this.transactionOutputService = transactionOutputService;
        this.restApiConfig = configLoader.getApi();
    }

    @RequestMapping(value = "/block/lasthash", method = RequestMethod.GET)
    public String getCurrentBlockHash() {
        return blockMapService.getLastBlock().getHash();
    }

    @RequestMapping(value = "/block/all", method = RequestMethod.GET)
    public LinkedHashSet<Block> getAllBlocks() {
//
//        ObjectMapper om = new ObjectMapper();
//        TransactionOutput transaction = transactionOutputService.getAll().stream().findFirst().get();
//        try {
//            String test = om.writeValueAsString(transaction);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        return blockMapService.getAll();
    }

    @RequestMapping(value = "/block/allafter/{hash}", method = RequestMethod.GET)
    public LinkedHashSet<Block> getAllFrom(@PathVariable("hash") String hash) {

        return blockMapService.getBlocksAfter(hash);
    }

    @RequestMapping(value = "/block/add", method = RequestMethod.POST, consumes = JSON)
    public Block addBlockToChain(@RequestBody BlockVerified blockDto) {
        Block lastBlock = blockMapService.getLastBlock();

        boolean blockValid = Validation.isBlockValid(lastBlock, blockDto);
        if (blockValid) {
            blockMapService.save(blockDto);
        }
        return null;
        // Wygenerowanie nowego UTXO!? - > w którym momencie
    }

    @RequestMapping(value = "/block/mining/stop", consumes = JSON, method = RequestMethod.POST)
    public void stopMining(@RequestBody StopMsg stopMsg) {
        System.out.println();
        LOG.info("Hash come: " + stopMsg.getSecret());
        Miner.mining = false;
    }
}
