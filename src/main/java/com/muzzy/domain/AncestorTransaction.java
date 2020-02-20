package com.muzzy.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.ArrayList;

@Setter
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AncestorTransaction extends Transaction implements Serializable {

    @Builder(builderMethodName = "childBuilder")
    public AncestorTransaction(String sender, String receiver, BigDecimal value, ArrayList<TransactionInput> inputs, ArrayList<TransactionOutput> outputs, String transactionId) {
        super(sender, receiver, value, inputs, outputs, transactionId);
    }

}
