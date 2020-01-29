package com.muzzy.roles;

import com.muzzy.Main;
import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Miner implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Miner.class);
    private Integer tNumber;
    private Block block;
    private int DIFFICULTY = 6;

    private void stop() {
        Main.isStart = false;
    }

    public Miner(Integer tNumber, Block block) {
        this.tNumber = tNumber;
        this.block = block;
    }

    @Override
    public void run() {
        Main.isStart = true;
//        getSystemInfo();
        //        long endTime = System.currentTimeMillis();
        LOG.info("Starting thread" + tNumber);
        mine(DIFFICULTY);



        //        long startTime= System.currentTimeMillis();
//        block.mine(DIFFICULTY);
//        long endTime = System.currentTimeMillis();
//        if (endTime - startTime < 10000 ){
//            difficulty++;
//        }
//        else if (endTime - startTime > 10000 ){
//            difficulty--;
//        }

    }



    public static void getSystemInfo(){
        long maxMemory = Runtime.getRuntime().maxMemory();

        LOG.info("Operating system: "+ System.getProperty("os.name"));
        LOG.info("System architecture: " + System.getProperty("os.arch"));
        LOG.info("Operating system version: " +System.getProperty("osVersion"));
        LOG.info("");
        LOG.info("Available processors (cores): " +Runtime.getRuntime().availableProcessors());
        LOG.info("Free memory (bytes):" + Runtime.getRuntime().freeMemory());
        LOG.info("Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
        LOG.info("Total memory (bytes): " + Runtime.getRuntime().totalMemory());
    }

    public void mine(int difficulty) {
        Integer nonce = 0;
        String hash = "";
        String toHash = block.getPreviousHash() + block.getTimestamp() + block.getTransactions();
        do {
            nonce = (int) (Math.random() * 10000000);
            hash = StringUtil.applySha256(toHash + nonce);
        } while (!hash.substring(0, difficulty).matches("[0]{" + difficulty + "}") && Main.isStart == true);
        System.out.println(Main.isStart);
        Main.isStart = false;
        if(hash.substring(0, difficulty).matches("[0]{" + difficulty + "}")) {
            LOG.info("Done: " + hash + " by Thread no. " + tNumber);
        }
    }
}
