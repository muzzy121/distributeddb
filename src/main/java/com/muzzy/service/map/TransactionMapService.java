package com.muzzy.service.map;

import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.service.TransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Service
public class TransactionMapService extends AbstractTransactionMapService<Transaction, String> implements TransactionService {
    @Override
    public LinkedHashSet<Transaction> getAll() {
        return super.findAll();
    }

    @Override
    public Transaction getById(String id) {
        return super.findById(id);
    }

    @Override
    public Transaction save(Transaction t) {
        return super.save(t);
    }

    @Override
    public void delete(Transaction t) {
        super.delete(t);
    }

    @Override
    public void deleteById(String id) {
        super.deleteById(id);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void save(Set<Transaction> transactions) {
        transactions.forEach(this::save);
        // How to verify Transactions (overspend.. etc..)
    }

    @Override
    public BigDecimal getValueOfTransactionsBySender(String sender) {
        return new BigDecimal(getAll().stream()
                .filter(transaction -> transaction.getSender().equals(sender)) //Stream<Transaction>
                .map(transaction ->transaction.getValue()) //Stream<BigDecimal>
                .collect(Collectors.summingLong(BigDecimal::longValue)));
    }

    @Override
    public List<TransactionOutput> getTransactionOutputsFromLastTransactionBySender(String sender) {
        Transaction transaction = this.getAll().stream()
                .filter(x -> x.getSender().equals(sender))
                .reduce((f,l) -> l).orElse(null);
        if (transaction !=null) {
            List<TransactionOutput> transactionOutputs = transaction.getOutputs().stream()
                    .filter(t -> t.getReceiver().equals(sender)).collect(Collectors.toList());
            return transactionOutputs;
        }
        return null;
    }
}
