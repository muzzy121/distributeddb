package com.muzzy.bootstrap;

import com.muzzy.domain.*;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.controllerservice.test.Wallet;
import com.muzzy.service.map.BlockMapService;
import com.muzzy.service.map.WalletMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Load simple data for Functional Tests
 */

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {
    final static Logger LOG = LoggerFactory.getLogger(Bootstrap.class);
    final static int DIFFICULTY = 4;

    private final BlockMapService blockMapService;
    private final TransactionOutputService transactionOutputService;
    private final WalletMapService walletMapService;


    @Autowired
    public Bootstrap(BlockMapService blockMapService, TransactionOutputService transactionOutputService, WalletMapService walletMapService) {
        this.blockMapService = blockMapService;
        this.transactionOutputService = transactionOutputService;
        this.walletMapService = walletMapService;
    }

//    private Wallet walletA;
//    private Wallet walletB;
//    private Wallet walletC;
//    private Wallet ancestorWallet;
//    private Transaction ancestorTransaction;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {


        Wallet ancestorWallet = new Wallet();
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();
        Wallet walletC = new Wallet();

        Block genesis = new BlockVerified("0");
        LOG.info("Creating and Mining first block... ");


        Transaction ancestorTransaction = new AncestorTransaction().init(ancestorWallet, walletA.getPublicKey(), 100F);
        transactionOutputService.save(ancestorTransaction.getOutputs().get(0));

        genesis.addTransaction(ancestorTransaction);

//        genesis.addTransaction(walletA.sendFunds(walletC.getPublicKey(), 10f));
        addBlock(genesis);

        Block block1 = new BlockVerified(blockMapService.getLastBlock().getHash());

        LOG.info("\nWalletA's balance is: " + walletA.getBalance());

        block1.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 40f));
        block1.addTransaction(walletA.sendFunds(walletC.getPublicKey(), 10f));

        addBlock(block1);
        LOG.info("\nWalletA's balance is: " + walletA.getBalance());
        LOG.info("WalletB's balance is: " + walletB.getBalance());

        Block block2 = new BlockVerified(blockMapService.getLastBlock().getHash());

        block2.addTransaction(walletA.sendFunds(walletC.getPublicKey(), 40f));
        addBlock(block2);
        LOG.info("\nWalletA's balance is: " + walletA.getBalance());
        LOG.info("WalletC's balance is: " + walletC.getBalance());


        Block block3 = new BlockVerified(blockMapService.getLastBlock().getHash());
        block3.addTransaction(walletB.sendFunds(walletC.getPublicKey(), 20f));
        addBlock(block3);
        LOG.info("\nWalletC's balance is: " + walletC.getBalance());
        LOG.info("WalletB's balance is: " + walletB.getBalance());

        Block block4 = new BlockVerified(blockMapService.getLastBlock().getHash());
        block4.addTransaction(walletC.sendFunds(walletB.getPublicKey(), 60f));
        addBlock(block4);
        LOG.info("\nWalletC's balance is: " + walletC.getBalance());
        LOG.info("WalletB's balance is: " + walletB.getBalance());
//
//        Validation.isChainValid(ancestorTransaction, difficulty, blockchain);

    }

    public void addBlock(Block block) {
//        long startTime= System.currentTimeMillis();
        block.mine(DIFFICULTY);
//        long endTime = System.currentTimeMillis();
//        if (endTime - startTime < 10000 ){
//            difficulty++;
//        }
//        else if (endTime - startTime > 10000 ){
//            difficulty--;
//        }
        blockMapService.save(block);
    }


}
