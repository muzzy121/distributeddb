package com.muzzy.domain;

import com.muzzy.domain.spsfl.SerialVersionUIDContainer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TransactionInput implements Serializable {

    private static final long serialVersionUID = SerialVersionUIDContainer.TRANSACTION_INPUT_SVUID;

    private String transactionOutputId;
    private TransactionOutput utxo;

    public TransactionInput(String transactionOutputId, TransactionOutput utxo) {
        this.transactionOutputId = transactionOutputId;
        this.utxo = utxo;
    }

}