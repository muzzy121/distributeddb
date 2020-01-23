package com.muzzy.domain;

import com.muzzy.service.controllerservice.test.Wallet;

import java.security.PublicKey;

public class AncestorTransaction extends Transaction {
    private Wallet ancestorWallet = new Wallet();

    public AncestorTransaction() {
        super();
    }

    public Transaction init(PublicKey receiver, float value) {
        super.setSender(ancestorWallet.getPublicKey());
        super.setReceiver(receiver);
        super.setValue(value);
        super.setInputs(null);
        super.generateSignature(ancestorWallet.getPrivateKey());
        super.setTransactionId("0");
        super.getOutputs().add(new TransactionOutput(super.getReceiver(), super.getValue(), super.getTransactionId()));
        return this;
    }
}
