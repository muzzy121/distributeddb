package com.muzzy.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.ArrayList;

@Setter
@Getter
public class AncestorTransaction extends Transaction {

    @Builder(builderMethodName = "childBuilder")
    public AncestorTransaction(PublicKey sender, PublicKey reciever, BigDecimal value, ArrayList<TransactionInput> inputs, ArrayList<TransactionOutput> outputs, String transactionId) {
        super(sender, reciever, value, inputs, outputs, transactionId);
    }

}
