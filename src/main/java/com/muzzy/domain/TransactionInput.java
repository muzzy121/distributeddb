package com.muzzy.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionInput {
    private String transactionOutputId;
    private TransactionOutput utxo;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
    public TransactionInput(String transactionOutputId, TransactionOutput utxo) {
        this.transactionOutputId = transactionOutputId;
        this.utxo = utxo;
    }

}