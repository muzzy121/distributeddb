package com.muzzy.service.factory;

import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.AncestorTransaction;
import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.domain.Wallet;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.controllerservice.Validation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AncestorTransactionFactory {
    private final TransactionOutputService transactionOutputService;
    private AncestorTransaction ancestorTransaction;
    public AncestorTransactionFactory(TransactionOutputService transactionOutputService) {
        this.transactionOutputService = transactionOutputService;
    }

    public Transaction getAncestorTransaction(Wallet ancestorWallet, String receiver, BigDecimal value) {
        ancestorTransaction = AncestorTransaction.childBuilder()
                .sender(ancestorWallet.getPublicKey())
                .receiver(receiver)
                .value(value)
                .inputs(null)
                .transactionId("0")
                .build();
        ancestorTransaction.setTransactionId(calculateHash());
        ancestorTransaction.getOutputs().add(new TransactionOutput(receiver, value, ancestorTransaction.getTransactionId()));
        ancestorTransaction.setSignature(generateSignature(ancestorWallet.getPrivateKey(),ancestorTransaction));
        return ancestorTransaction;
    }
    public byte[] generateSignature(String privateKey, Transaction t) {
        // TODO: 2020-01-23 Czy sygnatura nie powinna być z datą? Może dodać Pole daty do transakcji, jej utworzenia
        String data = t.getSender() + t.getReceiver() + t.getValue();
        return Validation.confirm(privateKey, data);
    }
    private String calculateHash() {
        return StringUtil.applySha256(
                 ancestorTransaction.getSender() +
                        ancestorTransaction.getReceiver() +
                        ancestorTransaction.getValue()
        );
    }
}