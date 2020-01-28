package com.muzzy.domain;

import com.muzzy.service.TransactionOutputService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.security.PublicKey;
import java.util.ArrayList;

@Setter
@Getter
public class AncestorTransaction extends Transaction {

//    public AncestorTransaction() {
//
//        super();
//    }
    @Builder(builderMethodName = "childBuilder")
    public AncestorTransaction(PublicKey sender, PublicKey reciever, float value, ArrayList<TransactionInput> inputs,ArrayList<TransactionOutput> outputs, String transactionId) {
        super(sender, reciever, value, inputs, outputs, transactionId);
    }
    //    public Transaction getAncestorTransaction(Wallet ancestorWallet, PublicKey receiver, float value) {
//        super.setSender(ancestorWallet.getPublicKey());
//        super.setReceiver(receiver);
//        super.setValue(value);
//        super.setInputs(null);
//        super.generateSignature(ancestorWallet.getPrivateKey());
//        super.setTransactionId("0");
//        super.getOutputs().add(new TransactionOutput(super.getReceiver(), super.getValue(), super.getTransactionId()));
//        return this;
//    }
}
