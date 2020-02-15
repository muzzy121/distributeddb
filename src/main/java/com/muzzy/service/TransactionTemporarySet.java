package com.muzzy.service;

import com.muzzy.domain.TransactionOutput;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Component
public class TransactionTemporarySet {
    private Set<TransactionOutput> transactionOutputSet = new HashSet<>();

    public void cleanAll() {
        transactionOutputSet.clear();
    }

    public void addTransaction(TransactionOutput transactionOutput) {
        transactionOutputSet.add(transactionOutput);
    }
}
