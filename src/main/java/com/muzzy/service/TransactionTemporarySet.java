package com.muzzy.service;

import com.muzzy.domain.TransactionOutput;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@Getter
@Setter
@Component
public class TransactionTemporarySet {
    private ArrayList<TransactionOutput> transactionOutputSet = new ArrayList<>();

    public void cleanAll(){
        transactionOutputSet.clear();
    }
    public void addTransaction(TransactionOutput transactionOutput){
        transactionOutputSet.add(transactionOutput);
    }

}
