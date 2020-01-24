package com.muzzy.roles;

import com.muzzy.domain.*;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.controllerservice.test.Wallet;
import com.muzzy.service.map.BlockMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BlockChain implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * Moje uwagi
     * - bezsensu jest addTrans w klasie Block
     * - Refactor
     *
     */

    //DLA USPOKOJENIA KOMPILATORA
    public static ArrayList<Block> blockchain = new ArrayList<>();


    private final BlockMapService blockMapService;
    private final TransactionOutputService transactionOutputService;


    @Autowired
    public BlockChain(BlockMapService blockMapService, TransactionOutputService transactionOutputService) {
        this.blockMapService = blockMapService;
        this.transactionOutputService = transactionOutputService;
    }

    public static int difficulty = 4;
    private Wallet walletA;
    private Wallet walletB;
    private Wallet walletC;
    private Wallet ancestorWallet;

    public static Transaction ancestorTransaction;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        ancestorWallet = new Wallet(transactionOutputService);
        walletA = new Wallet(transactionOutputService);
        walletB = new Wallet(transactionOutputService);
        walletC = new Wallet(transactionOutputService);

        ancestorTransaction = new AncestorTransaction().init(ancestorWallet, walletA.getPublicKey(), 100F); // tworze transakcje (z pierdoł poprawić wysyłanie tylko adresu portfela)
        transactionOutputService.save(ancestorTransaction.getOutputs().get(0)); //oraz dodaje ta transakcje do UTXOs

        System.out.println("Creating and Mining first block... ");

        Block genesis = new BlockVerified("0");  //tworze blok i minuje

        genesis.addTransaction(ancestorTransaction); //dodaje do bloku transakcje!?
        addBlock(genesis);

        if(!blockchain.get(blockchain.size() - 1).getHash().equals(blockMapService.getLastBlock().getHash())) {
            System.out.println("ERROR!!!");
        }

        Block block1 = new BlockVerified(blockMapService.getLastBlock().getHash());

        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        block1.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 40f));
        block1.addTransaction(walletA.sendFunds(walletC.getPublicKey(), 10f));
        addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        if(!blockchain.get(blockchain.size() - 1).getHash().equals(blockMapService.getLastBlock().getHash())) {
            System.out.println("ERROR!!!");
        }
        Block block2 = new BlockVerified(blockMapService.getLastBlock().getHash());

        block2.addTransaction(walletA.sendFunds(walletC.getPublicKey(), 40f));
        addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletC's balance is: " + walletC.getBalance());

        if(!blockchain.get(blockchain.size() - 1).getHash().equals(blockMapService.getLastBlock().getHash())) {
            System.out.println("ERROR!!!");
        }

        Block block3 = new BlockVerified(blockMapService.getLastBlock().getHash());
        block3.addTransaction(walletB.sendFunds(walletC.getPublicKey(), 20f));
        addBlock(block3);
        System.out.println("\nWalletC's balance is: " + walletC.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        if(!blockchain.get(blockchain.size() - 1).getHash().equals(blockMapService.getLastBlock().getHash())) {
            System.out.println("ERROR!!!");
        }

        Block block4 = new BlockVerified(blockMapService.getLastBlock().getHash());
        block4.addTransaction(walletC.sendFunds(walletB.getPublicKey(), 60f));
        addBlock(block4);
        System.out.println("\nWalletC's balance is: " + walletC.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());
//
//        Validation.isChainValid(ancestorTransaction, difficulty, blockchain);

    }

    public void addBlock(Block block) {
//        long startTime= System.currentTimeMillis();
        block.mine(difficulty);
//        long endTime = System.currentTimeMillis();
//        if (endTime - startTime < 10000 ){
//            difficulty++;
//        }
//        else if (endTime - startTime > 10000 ){
//            difficulty--;
//        }
        blockchain.add(block);
        blockMapService.save(block);
    }


}
