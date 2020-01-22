package com.muzzy.roles;

import com.muzzy.domain.*;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.controllerservice.test.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();  //Ta lista nie moze działać w ten sposób, to musi być serwis



    private final TransactionOutputService transactionOutputService;


    @Autowired
    public BlockChain(TransactionOutputService transactionOutputService) {
        this.transactionOutputService = transactionOutputService;
    }

    public static int difficulty = 4;
    public static Wallet walletA;
    public static Wallet walletB;
    public static Wallet walletc;
    public static Transaction ancestorTransaction;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {


        walletA = new Wallet();
        walletB = new Wallet();
        walletc = new Wallet();

        ancestorTransaction = new AncestorTransaction().init(walletA, 100F); // tworze transakcje (
        transactionOutputService.save(ancestorTransaction.outputs.get(0).id,ancestorTransaction.outputs.get(0)); //oraz dodaje ta transakcje do UTXOs

        System.out.println("Creating and Mining first block... ");

        Block genesis = new BlockVerified("0");  //tworze blok i minuje



//        genesis.addTransaction(ancestorTransaction, UTXOs); //dodaje do bloku transakcje!?
//        addBlock(genesis);
//
//        Block block1 = new Block(blockchain.get(blockchain.size() - 1).hash);
//        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//
//        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f), UTXOs);
//        addBlock(block1);
//        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//        System.out.println("WalletB's balance is: " + walletB.getBalance());
//
//        Block block2 = new Block(blockchain.get(blockchain.size() - 1).hash);
//        block2.addTransaction(walletA.sendFunds(walletc.publicKey, 40f), UTXOs);
//        addBlock(block2);
//        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
//        System.out.println("WalletC's balance is: " + walletc.getBalance());
//
//        Block block3 = new Block(blockchain.get(blockchain.size() - 1).hash);
//        block3.addTransaction(walletB.sendFunds(walletc.publicKey, 20f), UTXOs);
//        addBlock(block3);
//        System.out.println("\nWalletC's balance is: " + walletc.getBalance());
//        System.out.println("WalletB's balance is: " + walletB.getBalance());
//
//        Block block4 = new Block(blockchain.get(blockchain.size() - 1).hash);
//        block4.addTransaction(walletc.sendFunds(walletB.publicKey, 60f), UTXOs);
//        addBlock(block4);
//        System.out.println("\nWalletC's balance is: " + walletc.getBalance());
//        System.out.println("WalletB's balance is: " + walletB.getBalance());
//
//        Validation.isChainValid(ancestorTransaction, difficulty, blockchain);

    }

    public static void addBlock(Block block) {
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
    }


}
