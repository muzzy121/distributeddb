package com.muzzy.roles;

import com.muzzy.Main;
import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.Block;
import com.muzzy.domain.BlockVerified;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.TransactionService;
import com.muzzy.service.TransactionTemporarySet;
import com.muzzy.service.map.BlockMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class Miner implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Miner.class);
    private String tNumber = "";
    private int DIFFICULTY = 5;
    private Block block;
    private String hash = "999999999";
    private static String hashTmp = "";
    private static boolean mining = true;
    private static long lag = 0L;
    private String previousHash;

    private final BlockMapService blockMapService;
    private final TransactionService transactionService;
    private final TransactionTemporarySet transactionTemporarySet;
    private final TransactionOutputService transactionOutputService;



    private void stop() {
        Main.isStart = false;
    }

    public Miner(BlockMapService blockMapService, TransactionService transactionService, TransactionTemporarySet transactionTemporarySet, TransactionOutputService transactionOutputService) {
        this.blockMapService = blockMapService;
        this.transactionService = transactionService;
        this.previousHash = blockMapService.getLastBlock().getHash();
        this.transactionTemporarySet = transactionTemporarySet;
        this.transactionOutputService = transactionOutputService;
    }

    @Override
    public void run() {
        MineRunner.notMined = false;
        block = new BlockVerified(previousHash);
        block.setTransactions(transactionService.getAll());
        transactionService.clear();

        mine(DIFFICULTY);
        if (!block.getTransactions().isEmpty()) {
            LOG.warn("Added " + block.getTransactions().size() + " transactions");
        }
        MineRunner.notMined = true;
    }
    public static void getSystemInfo() {
        long maxMemory = Runtime.getRuntime().maxMemory();

        LOG.info("Operating system: " + System.getProperty("os.name"));
        LOG.info("System architecture: " + System.getProperty("os.arch"));
        LOG.info("Operating system version: " + System.getProperty("os.Version"));
        LOG.info("");
        LOG.info("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
        LOG.info("Free memory (bytes):" + Runtime.getRuntime().freeMemory());
        LOG.info("Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
        LOG.info("Total memory (bytes): " + Runtime.getRuntime().totalMemory());
    }

    public Block mine(int difficulty) {
        mining = true;
        int a = 0;
        Integer nonce = 0;
        long startTime = System.currentTimeMillis();
        String toHash = block.getPreviousHash() + block.getTimestamp() + block.getTransactions();
        do {
            nonce = (int) (Math.random() * 10000000);
            hashTmp = StringUtil.applySha256(toHash + nonce);
            if (hashTmp.substring(0, difficulty).matches("[0]{" + difficulty + "}")) {
                hash = hashTmp;
            }
        } while (!hash.substring(0, difficulty).matches("[0]{" + difficulty + "}") && mining == true) ;
        mining =  false;
        long endTime = System.currentTimeMillis();
        if (hash.substring(0, difficulty).matches("[0]{" + difficulty + "}")) {
            long hashTime = endTime - startTime;
            LOG.info("Hash found: " + hash + " in: " + hashTime / 1000 + "sec.");
            block.setHashTime(hashTime);
            block.setHash(hash);
            if (!block.getTransactions().isEmpty()) {
                blockMapService.save(block);
                transactionTemporarySet.getTransactionOutputSet().forEach(t -> transactionOutputService.save(t));
                transactionTemporarySet.cleanAll();
            }
            return block;
        } else return null;
    }
}
