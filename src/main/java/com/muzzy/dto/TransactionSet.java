package com.muzzy.dto;

import com.muzzy.domain.Transaction;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Getter
public class TransactionSet implements Serializable {

    private Set<Transaction> transactions;

    public TransactionSet() {
        this.transactions = new HashSet<>();
    }


}
