package com.muzzy.service.controllerservice.test;

import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionInput;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.service.TransactionOutputService;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
//import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    // When you call getBalance create local transaction table with this wallet transactions
    public HashMap<String, TransactionOutput> localUTXOs = new HashMap<>();

    @Autowired
    private TransactionOutputService transactionService;

    public Wallet() {
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(512);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float getBalance() {
        Set<TransactionOutput> transactionOutputSet = transactionService.getAll();
        double total = transactionOutputSet.stream()
                .filter(utxo -> utxo.isMine(publicKey))
                .map(y -> localUTXOs.put(y.id,y)) //To jest mega dziwne rozwiązanie ... bo getBalance robi wydaje mi się dwie różne rzeczy, nie tylko Balance
                .collect(Collectors.summingDouble(TransactionOutput::getValue));
        return (float) total;
    }

    public Transaction sendFunds(PublicKey receiver, float value) {
        if (getBalance() < value) {
            System.out.println("You don't have coins for this transaction");
            return null;
        }

        ArrayList<TransactionInput> inputs = new ArrayList<>();
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : localUTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if (total > value) break;
        }
        Transaction newTransaction = new Transaction(publicKey, receiver, value, inputs);
        newTransaction.generateSignature(privateKey);
        for (TransactionInput input : inputs) {
            localUTXOs.remove(input.transactionOutputId);
        }
        return newTransaction;
    }
}