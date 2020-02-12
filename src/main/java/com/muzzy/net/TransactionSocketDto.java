package com.muzzy.net;

import com.muzzy.domain.Transaction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Component
public class TransactionSocketDto implements Serializable, Sendable {
    private List<Transaction> transactions = new ArrayList<>();
    private static Long id = 0L;

    public TransactionSocketDto() {
        this.id = id +1L;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void phrase() {

    }

    public void addTransaction(Transaction t){
        transactions.add(t);
    }
    public void addTransaction(Set<Transaction> tSet){
        tSet.forEach(transactions::add);
    }
    public void addTransaction(List<Transaction> tList){
        transactions.addAll(tList);
    }
}

