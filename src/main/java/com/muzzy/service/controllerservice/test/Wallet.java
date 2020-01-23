package com.muzzy.service.controllerservice.test;

import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionInput;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.service.TransactionOutputService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;
@Getter
@Setter
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    // When you call getBalance create local transaction table with this wallet transactions

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
//                .map(y -> localUTXOs.put(y.id,y)) //To jest mega dziwne rozwiązanie ... bo getBalance robi wydaje mi się dwie różne rzeczy, nie tylko Balance
                .collect(Collectors.summingDouble(TransactionOutput::getValue));
        return (float) total;
    }


    // To jest tak naprawdę TransactionFactory
    public Transaction sendFunds(PublicKey receiver, float value) {
        //Quick break!
        if (getBalance() < value) {
            System.out.println("You don't have coins for this transaction");
            return null;
        }

        // Needed obj.
        Set<TransactionOutput> transactionOutputSet = transactionService.getTransctionByPublicKey(publicKey);
//        HashMap<String, TransactionOutput> localUTXOs = new HashMap<>();
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        float total = 0F;

        for (TransactionOutput utxo: transactionOutputSet
             ) {
            inputs.add(new TransactionInput(utxo.getId(),utxo));
            total += utxo.getValue();
            if (total > value) break;
        }




//        for (Map.Entry<String, TransactionOutput> item : localUTXOs.entrySet()) {
//
//            TransactionOutput UTXO = item.getValue();
//            total += UTXO.value;
//            inputs.add(new TransactionInput(UTXO.id));
//
//        }
        Transaction transaction = new Transaction(publicKey, receiver, value, inputs);
        transaction.generateSignature(privateKey);


        // Co tak naprawdę to robi!?
//        for (TransactionInput input : inputs) {
//            localUTXOs.remove(input.transactionOutputId);
//        }
//        localUTXOs.clear();
        return transaction;
    }
}