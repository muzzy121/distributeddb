package com.muzzy.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;

@Setter
@Getter
public class AncestorTransaction extends Transaction {

    @Builder(builderMethodName = "childBuilder")
    public AncestorTransaction(String sender, String receiver, BigDecimal value, ArrayList<TransactionInput> inputs, ArrayList<TransactionOutput> outputs, String transactionId) {
        super(sender, receiver, value, inputs, outputs, transactionId);
    }

}
