package com.muzzy.bootstrap;

import com.muzzy.Main;
import com.muzzy.domain.*;
import com.muzzy.net.api.RESTApiControl;
import com.muzzy.roles.MineRunner;
import com.muzzy.roles.Miner;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.TransactionService;
import com.muzzy.service.TransactionTemporarySet;
import com.muzzy.service.controllerservice.Validation;
import com.muzzy.service.factory.AncestorTransactionFactory;
import com.muzzy.service.factory.TransactionFactory;
import com.muzzy.service.map.BlockMapService;
import com.muzzy.service.map.WalletMapService;
import com.muzzy.utils.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Load simple data for Functional Tests
 */

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {
    final static Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    private final URL urlBlockchain = getClass().getResource("/blockchain/block.chain");
    private final URL urlUtxo = getClass().getResource("/blockchain/utxo.chain");

    private final BlockMapService blockMapService;
    private final WalletMapService walletMapService;
    private final TransactionFactory transactionFactory;
    private final AncestorTransactionFactory ancestorTransactionFactory;
    private final TransactionService transactionService;
    private final TransactionOutputService transactionOutputService;
    private final TransactionTemporarySet transactionTemporarySet;
    private final RESTApiControl restApiControl;
    private ExecutorService executorService;
    private Future future;
    @Autowired
    private ApplicationContext context;

    @Autowired
    public Bootstrap(BlockMapService blockMapService, TransactionOutputService transactionOutputService, WalletMapService walletMapService, TransactionFactory transactionFactory, AncestorTransactionFactory ancestorTransactionFactory, TransactionService transactionService, RESTApiControl restApiControl, Serializer serializer, TransactionTemporarySet transactionTemporarySet) {
        this.blockMapService = blockMapService;
        this.transactionOutputService = transactionOutputService;
        this.walletMapService = walletMapService;
        this.transactionFactory = transactionFactory;
        this.ancestorTransactionFactory = ancestorTransactionFactory;
        this.transactionService = transactionService;
        this.restApiControl = restApiControl;
        this.transactionTemporarySet = transactionTemporarySet;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Wallet ancestorWallet = new Wallet();
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();
        Wallet walletC = new Wallet();

        Main.nodeId = UUID.randomUUID();
        Miner.getSystemInfo();
//      Add wallets to repository - for tests
        walletMapService.save(walletA);
        walletMapService.save(walletB);
        walletMapService.save(walletC);

        /**
         * Getting blocks stored on disk
         */
        Serializer<ArrayList<TransactionOutput>> utxoSerializer = new Serializer<>();
        ArrayList<TransactionOutput> utxoDeserialize = new ArrayList<>();
        if (utxoSerializer.deserialize(urlUtxo) != null) {
            utxoDeserialize.addAll(utxoSerializer.deserialize(urlUtxo));
            transactionOutputService.save(utxoDeserialize);
        }

        /**
         * Downloading transactions
         */
        Set<TransactionOutput> allUtxo = restApiControl.getAllUtxo();
        if(allUtxo != null) {
            transactionOutputService.clear();
            transactionOutputService.save(allUtxo);
        }

        /**
         * Getting blocks stored on disk
         */
        Serializer<LinkedHashSet<BlockVerified>> serializer = new Serializer<>();
        LinkedHashSet<BlockVerified> deserialize = new LinkedHashSet<>();
        if (serializer.deserialize(urlBlockchain) != null) {
            deserialize.addAll(serializer.deserialize(urlBlockchain));
            blockMapService.save(deserialize);
        }
        /**
         * Downloading block
         */

        Block block = blockMapService.getLastBlock();
        if (block != null) {
            blockMapService.save(restApiControl.getBlocksFromNetworkAfter(block.getHash()));
        } else {
            blockMapService.save(restApiControl.getBlocksFromNetwork());
        }

        /**
         *
         * Test purpose - adding first transacrions and blocks
         */

        if (blockMapService.getAll().size() == 0) {
            Block genesis = new BlockVerified("0");
            LOG.info("Creating and Mining first block... ");
//------------------
            Transaction ancestorTransaction = ancestorTransactionFactory.getAncestorTransaction(ancestorWallet, walletA.getPublicKey(), BigDecimal.valueOf(100));
            transactionOutputService.save(ancestorTransaction.getOutputs().get(0));
            genesis.addTransaction(ancestorTransaction);
            genesis.mine(Main.DIFFICULTY);
            blockMapService.save(genesis);
            LOG.info("WalletA's balance before: " + transactionOutputService.getBalance(walletA.getPublicKey()));
//------------------
            startMining();
            pauseBeforeTransactions();

            addTransaction(transactionFactory.getTransaction(walletA.getPrivateKey(), walletA.getPublicKey(), walletB.getPublicKey(), BigDecimal.valueOf(40F)));
            addTransaction(transactionFactory.getTransaction(walletB.getPrivateKey(), walletB.getPublicKey(), walletC.getPublicKey(), BigDecimal.valueOf(20F)));
            addTransaction(transactionFactory.getTransaction(walletA.getPrivateKey(), walletA.getPublicKey(), walletC.getPublicKey(), BigDecimal.valueOf(10F)));

            LOG.debug("WalletA's balance is: " + transactionOutputService.getBalance(walletA.getPublicKey()));
            LOG.debug("WalletB's balance is: " + transactionOutputService.getBalance(walletB.getPublicKey()));
            LOG.debug("WalletC's balance is: " + transactionOutputService.getBalance(walletC.getPublicKey()));

//-----------------
            pauseBeforeTransactions();
            addTransaction(transactionFactory.getTransaction(walletA.getPrivateKey(), walletA.getPublicKey(), walletC.getPublicKey(), BigDecimal.valueOf(40F)));
            LOG.debug("WalletA's balance is: " + transactionOutputService.getBalance(walletA.getPublicKey()));
            LOG.debug("WalletB's balance is: " + transactionOutputService.getBalance(walletB.getPublicKey()));
            LOG.debug("WalletC's balance is: " + transactionOutputService.getBalance(walletC.getPublicKey()));

//-----------------
            pauseBeforeTransactions();
            addTransaction(transactionFactory.getTransaction(walletB.getPrivateKey(), walletB.getPublicKey(), walletC.getPublicKey(), BigDecimal.valueOf(20F)));
            LOG.debug("WalletA's balance is: " + transactionOutputService.getBalance(walletA.getPublicKey()));
            LOG.debug("WalletB's balance is: " + transactionOutputService.getBalance(walletB.getPublicKey()));
            LOG.debug("WalletC's balance is: " + transactionOutputService.getBalance(walletC.getPublicKey()));


//-----------------
            pauseBeforeTransactions();
            addTransaction(transactionFactory.getTransaction(walletC.getPrivateKey(), walletC.getPublicKey(), walletB.getPublicKey(), BigDecimal.valueOf(60F)));
            addTransaction(ancestorTransactionFactory.getAncestorTransaction(ancestorWallet, walletA.getPublicKey(), BigDecimal.valueOf(100F)));
            LOG.info("WalletA's balance is: " + transactionOutputService.getBalance(walletA.getPublicKey()));
            LOG.info("WalletB's balance is: " + transactionOutputService.getBalance(walletB.getPublicKey()));
            LOG.info("WalletC's balance is: " + transactionOutputService.getBalance(walletC.getPublicKey()));
//-----------------
//        Validation.isChainValid(ancestorTransaction, difficulty, blockchain);
        }
    }

    @Deprecated
    public void startMining() {
        Main.isStart = true;
        executorService = Executors.newSingleThreadExecutor();
        future = executorService.submit(context.getBean(MineRunner.class));
        executorService.shutdown();

    }
    @Deprecated
    public void addTransaction(Transaction transaction) {
        if(transaction == null) { return; }
        LOG.info("Transaction add");
        String msg = transaction.getSender() + transaction.getReceiver() + transaction.getValue();
        boolean verifySignature = Validation.verifySignature(transaction.getSender(), msg, transaction.getSignature());
        if(verifySignature) {
            transactionService.save(transaction);
        }
    }
    @Deprecated
    public void pauseBeforeTransactions() {
        while (Miner.mining == true) {}
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
