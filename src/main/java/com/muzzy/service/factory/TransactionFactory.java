package com.muzzy.service.factory;

import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionInput;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.service.TransactionOutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@Component
public class TransactionFactory {
    @Autowired
    private TransactionOutputService transactionOutputService;

    public Transaction sendFunds(PrivateKey privateKey, PublicKey sender, PublicKey receiver, float value) {
        /**
         * Ten early exit także trzeba obgadać, chyba że poszukać transakcji która pozwoli zapłacić z całości!?
         * Co będzie wydajniejsze - czy lepiej eliminować małe transackcje z UTXO, czy lepiej wydawać z jednego inputa
         */
        if (transactionOutputService.getBalance(sender) < value) {
            System.out.println("You don't have coins for this transaction");
            return null;
        }
        /**
         * Sortowanie listy, trzeba się zastanowić czy jest ono potrzebne i jak wpływana na wydajność
         */
        List<TransactionOutput> transactionOutputList = new ArrayList<TransactionOutput>();
        transactionOutputService.getTransctionByPublicKey(sender).forEach(transactionOutput -> transactionOutputList.add(transactionOutput));
        Collections.sort(transactionOutputList);

        // Mandatory obj.
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        float total = 0F;


        // TODO: 2020-01-28 Trzeba sie zastanowić jak to ma działać
        for (TransactionOutput utxo: transactionOutputList
        ) {
            inputs.add(new TransactionInput(utxo.getId(),utxo));
            total += utxo.getValue();
            if (total >= value) break;
        }

        Transaction transaction = new Transaction().builder().sender(sender).reciever(receiver).value(value).inputs(inputs).transactionOutputService(transactionOutputService).build();
        transaction.generateSignature(privateKey);
        return transaction;

    }
}
