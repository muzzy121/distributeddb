package com.muzzy.service.factory;

import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.AncestorTransaction;
import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.domain.Wallet;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.controllerservice.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

@Component
public class AncestorTransactionFactory {
    @Autowired
    private TransactionOutputService transactionOutputService;

    public Transaction getAncestorTransaction(Wallet ancestorWallet, PublicKey receiver, float value) {

//        super.setSender(ancestorWallet.getPublicKey());
//        super.setReceiver(receiver);
//        super.setValue(value);
//        super.setInputs(null);
//        super.generateSignature(ancestorWallet.getPrivateKey());
//        super.setTransactionId("0");
//        super.getOutputs().add(new TransactionOutput(super.getReceiver(), super.getValue(), super.getTransactionId()));
//        new AncestorTransaction()
        ArrayList<TransactionOutput> transactionOutputs = new ArrayList<>();
        transactionOutputs.add(new TransactionOutput(receiver, value, "0"));
        AncestorTransaction ancestorTransaction = AncestorTransaction.childBuilder()
                .sender(ancestorWallet.getPublicKey())
                .reciever(receiver)
                .value(value)
                .inputs(null)
                .transactionId("0")
                .build();
        ancestorTransaction.getOutputs().add(new TransactionOutput(receiver, value, "0"));
        generateSignature(ancestorWallet.getPrivateKey(),ancestorTransaction);
        return ancestorTransaction;
    }
    public byte[] generateSignature(PrivateKey privateKey, Transaction t) {
        // TODO: 2020-01-23 Czy sygnatura nie powinna być z datą? Może dodać Pole daty do transakcji, jej utworzenia
        String data = StringUtil.getStringFromKey(t.getSender()) + StringUtil.getStringFromKey(t.getReceiver()) + t.getValue();
        return Validation.confirm(privateKey, data);
    }
}