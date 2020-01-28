package com.muzzy.service.factory;

import com.muzzy.domain.AncestorTransaction;
import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.domain.Wallet;
import com.muzzy.service.TransactionOutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                .outputs(transactionOutputs)
                .build();

//        ancestorTransaction.setOutputs(transactionOutputs);

        return ancestorTransaction;

    }
}