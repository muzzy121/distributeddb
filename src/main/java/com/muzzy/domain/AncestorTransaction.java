package com.muzzy.domain;

import java.security.PublicKey;

public class AncestorTransaction extends Transaction {

    public AncestorTransaction() {
        super();
    }

    public Transaction init(Wallet ancestorWallet, PublicKey receiver, float value) {
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
