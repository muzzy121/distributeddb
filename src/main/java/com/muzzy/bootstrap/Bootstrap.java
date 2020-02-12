package com.muzzy.bootstrap;

import com.muzzy.Main;
import com.muzzy.domain.*;
import com.muzzy.roles.Miner;
import com.muzzy.roles.OutgoingNode;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.domain.Wallet;
import com.muzzy.service.TransactionTemporarySet;
import com.muzzy.service.factory.AncestorTransactionFactory;
import com.muzzy.service.factory.TransactionFactory;
import com.muzzy.service.map.BlockMapService;
import com.muzzy.service.map.WalletMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Load simple data for Functional Tests
 */

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {
    final static Logger LOG = LoggerFactory.getLogger(Bootstrap.class);
    final static int DIFFICULTY = 1;

    private TaskExecutor taskExecutor;
    @Autowired
    private ApplicationContext context;

    private final BlockMapService blockMapService;
    private final WalletMapService walletMapService;
    private final TransactionFactory transactionFactory;
    private final AncestorTransactionFactory ancestorTransactionFactory;
    private final TransactionTemporarySet transactionTemporarySet;
    private final TransactionOutputService transactionOutputService;

    @Autowired
    public Bootstrap(BlockMapService blockMapService, TransactionOutputService transactionOutputService, WalletMapService walletMapService, TransactionFactory transactionFactory, AncestorTransactionFactory ancestorTransactionFactory, TransactionTemporarySet transactionTemporarySet) {
        this.blockMapService = blockMapService;
        this.transactionOutputService = transactionOutputService;
        this.walletMapService = walletMapService;
        this.transactionFactory = transactionFactory;
        this.ancestorTransactionFactory = ancestorTransactionFactory;
        this.transactionTemporarySet = transactionTemporarySet;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        Wallet ancestorWallet = new Wallet();
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();
        Wallet walletC = new Wallet();

        Block genesis = new BlockVerified("0");
        Miner.getSystemInfo();
        LOG.info("Creating and Mining first block... ");

//------------------
//        new AncestorTransaction().getAncestorTransaction(ancestorWallet, walletA.getPublicKey(), 100F);

        Transaction ancestorTransaction = ancestorTransactionFactory.getAncestorTransaction(ancestorWallet, walletA.getPublicKey(), 100F);

        //Druga operacja w bloku przechodzi, bp zapisuje Wyjście z transakcji na UTXO! O tutaj :)
        transactionOutputService.save(ancestorTransaction.getOutputs().get(0));
        genesis.addTransaction(ancestorTransaction);


        LOG.info("WalletA's balance before: " + transactionOutputService.getBalance(walletA.getPublicKey()));

        genesis.addTransaction(transactionFactory.getTransaction(walletA.getPrivateKey(), walletA.getPublicKey(), walletB.getPublicKey(), 10F));
        LOG.info("WalletA's balance is: " + transactionOutputService.getBalance(walletA.getPublicKey()));
        LOG.info("WalletB's balance is: " + transactionOutputService.getBalance(walletB.getPublicKey()));

        addBlock(genesis);
//------------------

        Block block1 = new BlockVerified(blockMapService.getLastBlock().getHash());

        block1.addTransaction(transactionFactory.getTransaction(walletA.getPrivateKey(), walletA.getPublicKey(), walletB.getPublicKey(), 40F));
        block1.addTransaction(transactionFactory.getTransaction(walletB.getPrivateKey(), walletB.getPublicKey(), walletC.getPublicKey(), 20F));
        block1.addTransaction(transactionFactory.getTransaction(walletA.getPrivateKey(), walletA.getPublicKey(), walletC.getPublicKey(), 10F));

        addBlock(block1);
        LOG.info("WalletA's balance is: " + transactionOutputService.getBalance(walletA.getPublicKey()));
        LOG.info("WalletB's balance is: " + transactionOutputService.getBalance(walletB.getPublicKey()));
        LOG.info("WalletC's balance is: " + transactionOutputService.getBalance(walletC.getPublicKey()));

//-----------------
        Block block2 = new BlockVerified(blockMapService.getLastBlock().getHash());

//        block2.addTransaction(transactionFactory.getTransaction(walletA.getPrivateKey(),walletA.getPublicKey(),walletC.getPublicKey(),40F));
        addBlock(block2);
        LOG.info("WalletA's balance is: " + transactionOutputService.getBalance(walletA.getPublicKey()));
        LOG.info("WalletC's balance is: " + transactionOutputService.getBalance(walletC.getPublicKey()));

//-----------------
        Block block3 = new BlockVerified(blockMapService.getLastBlock().getHash());
        block3.addTransaction(transactionFactory.getTransaction(walletB.getPrivateKey(), walletB.getPublicKey(), walletC.getPublicKey(), 20F));
        addBlock(block3);
        LOG.info("WalletB's balance is: " + transactionOutputService.getBalance(walletB.getPublicKey()));
        LOG.info("WalletC's balance is: " + transactionOutputService.getBalance(walletC.getPublicKey()));
//-----------------
        Block block4 = new BlockVerified(blockMapService.getLastBlock().getHash());
        block4.addTransaction(transactionFactory.getTransaction(walletC.getPrivateKey(), walletC.getPublicKey(), walletB.getPublicKey(), 60F));
        addBlock(block4);
        LOG.info("WalletB's balance is: " + transactionOutputService.getBalance(walletB.getPublicKey()));
        LOG.info("WalletC's balance is: " + transactionOutputService.getBalance(walletC.getPublicKey()));
//-----------------
//        Validation.isChainValid(ancestorTransaction, difficulty, blockchain);

//        Add wallets to repository - for tests
        walletMapService.save(walletA);
        walletMapService.save(walletB);
        walletMapService.save(walletC);

    }

    @Deprecated
    public void addBlock(Block block) {
//        long startTime= System.currentTimeMillis();

        block.mine(DIFFICULTY);
//        for(int cpu =0; cpu < Runtime.getRuntime().availableProcessors(); cpu++) {

//            new Thread(new Miner(cpu, block)).start();
//        }
//        try {
//            Thread.sleep(30*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        long endTime = System.currentTimeMillis();
//        if (endTime - startTime < 10000 ){
//            difficulty++;
//        }
//        else if (endTime - startTime > 10000 ){
//            difficulty--;
//        }

        if (blockMapService.save(block) == null) {
            LOG.info("Block has no transactions");
        }

        transactionTemporarySet.getTransactionOutputSet().forEach(t -> transactionOutputService.save(t));
        transactionTemporarySet.cleanAll();
    }


}
