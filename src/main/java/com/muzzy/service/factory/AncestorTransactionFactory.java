package com.muzzy.service.factory;

import com.muzzy.domain.AncestorTransaction;
import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.domain.Wallet;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.controllerservice.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class AncestorTransactionFactory {
    @Autowired
    private TransactionOutputService transactionOutputService;

    public Transaction getAncestorTransaction(Wallet ancestorWallet, String receiver, BigDecimal value) {
        ArrayList<TransactionOutput> transactionOutputs = new ArrayList<>();
        transactionOutputs.add(new TransactionOutput(receiver, value, "0"));
        AncestorTransaction ancestorTransaction = AncestorTransaction.childBuilder()
                .sender(ancestorWallet.getPublicKey())
                .receiver(receiver)
                .value(value)
                .inputs(null)
                .transactionId("0")
                .build();
        ancestorTransaction.getOutputs().add(new TransactionOutput(receiver, value, "0"));
        generateSignature(ancestorWallet.getPrivateKey(),ancestorTransaction);
        return ancestorTransaction;
    }
    public byte[] generateSignature(String privateKey, Transaction t) {
        // TODO: 2020-01-23 Czy sygnatura nie powinna być z datą? Może dodać Pole daty do transakcji, jej utworzenia
        String data = t.getSender() + t.getReceiver() + t.getValue();
        return Validation.confirm(privateKey, data);
    }
}