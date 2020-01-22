package com.muzzy.domain;

import com.muzzy.service.controllerservice.test.Wallet;

import java.util.HashMap;

public class AncestorTransaction extends Transaction {
    private Wallet ancestorWallet = new Wallet();

    public AncestorTransaction() {
        super();
    }

    public Transaction init(Wallet receiver, float value) {
        super.sender = ancestorWallet.publicKey;
        super.receiver = receiver.publicKey;
        super.value = value;
        super.inputs = null;
        super.generateSignature(ancestorWallet.privateKey);
        super.transactionId = "0";
        super.outputs.add(new TransactionOutput(super.receiver, super.value, super.transactionId));
        return this;
    }
}
