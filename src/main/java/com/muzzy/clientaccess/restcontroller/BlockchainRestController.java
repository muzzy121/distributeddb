package com.muzzy.clientaccess.restcontroller;

import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.Block;
import com.muzzy.dto.BlockDto;
import com.muzzy.net.commands.StopMsg;
import com.muzzy.roles.Miner;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.map.BlockMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
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
    public String getCurrentBlockHash() {
        return blockMapService.getLastBlock().getHash();
    }

    @RequestMapping(value = "/block/all", method = RequestMethod.GET)
    public LinkedHashSet<Block> getAllBlocks(){
//
//        ObjectMapper om = new ObjectMapper();
//        TransactionOutput transaction = transactionOutputService.getAll().stream().findFirst().get();
//        try {
//            String test = om.writeValueAsString(transaction);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        return blockMapService.getAll();
//        return null;
    }

    @RequestMapping(value = "/block/allafter/{hash}",method = RequestMethod.GET)
    public LinkedHashSet<Block> getAllFrom(@PathVariable("hash") String hash){

        return blockMapService.getBlocksAfter(hash);
    }

    @RequestMapping(value = "/block/add", method = RequestMethod.POST, consumes = JSON)
    public BlockDto addBlockToChain(@RequestBody BlockDto blockDto){
        Block block = blockMapService.getLastBlock();

        if(!block.getHash().equals(blockDto.getPreviousHash())){
            LOG.error("Bad previousHash code");
            return null;
        }
        String toHash = blockDto.getPreviousHash() + blockDto.getTimestamp() + blockDto.getTransactions();
        if(!blockDto.getHash().equals(StringUtil.applySha256(toHash + blockDto.getNonce()))){
            LOG.error("Bad hashCode!");
            return null;
        };
        if(blockDto.getTimestamp().isBefore(ZonedDateTime.now().minusMinutes(10))){
            LOG.error("Bad timestamp");
            return null;
        }
        return blockDto;

        //Validate block
        // czy hash jest OK
//        czy previousHash == lastHash w moim łańcuchu
//        czy proof of work się zgadza

// Wygenerowanie nowego UTXO!? - > w którym momencie


//        return null;
    }
    @RequestMapping(value = "/block/mining/stop", consumes = JSON, method = RequestMethod.POST)
    public void stopMining(@RequestBody StopMsg stopMsg){
        System.out.println();
        LOG.info("Hash come: "+stopMsg.getSecret());
        Miner.mining = false;
    }
}
