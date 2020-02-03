package com.muzzy.roles;

import com.muzzy.Main;
import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.Block;
import com.muzzy.domain.BlockVerified;
import com.muzzy.service.map.BlockMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Miner implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Miner.class);
    private Integer tNumber;
    private int DIFFICULTY = 5;
    private Block block;
    private final BlockMapService blockMapService;
    private static boolean isStart;

    private void stop() {isStart = false;
    }

    public Miner(Integer tNumber, BlockMapService blockMapService) {
        this.tNumber = tNumber;
        this.blockMapService = blockMapService;;
    }

    @Override
    public void run() {
        Main.notMined = true;
        LOG.info("Starting thread" + tNumber);
        block = new BlockVerified(blockMapService.getLastBlock().getPreviousHash());
        mine(DIFFICULTY);
    }

    public static void getSystemInfo(){
        long maxMemory = Runtime.getRuntime().maxMemory();

        LOG.info("Operating system: "+ System.getProperty("os.name"));
        LOG.info("System architecture: " + System.getProperty("os.arch"));
        LOG.info("Operating system version: " +System.getProperty("os.Version"));
        LOG.info("");
        LOG.info("Available processors (cores): " +Runtime.getRuntime().availableProcessors());
        LOG.info("Free memory (bytes):" + Runtime.getRuntime().freeMemory());
        LOG.info("Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
        LOG.info("Total memory (bytes): " + Runtime.getRuntime().totalMemory());
    }

    public void mine(int difficulty) {
//        LOG.debug("Hello from mine");
        Integer nonce = 0;
        long startTime= System.currentTimeMillis();
        String hash = "";
        String toHash = block.getPreviousHash() + block.getTimestamp() + block.getTransactions();
//        LOG.info(toHash);
        do {
            nonce = (int) (Math.random() * 10000000);
            hash = StringUtil.applySha256(toHash + nonce);
        } while (!hash.substring(0, difficulty).matches("[0]{" + difficulty + "}") && Main.notMined == true);
        Main.notMined = false;
        long endTime = System.currentTimeMillis();
        if(hash.substring(0, difficulty).matches("[0]{" + difficulty + "}")) {
            long hashTime = endTime - startTime;
            LOG.info("Done: " + hash + " by Thread no. " + tNumber + " in: " + hashTime/1000 + "sec." );
            block.setHashTime(hashTime);
        }
    }
}
