package com.muzzy.service;

import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionOutput;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */
@Service
public interface TransactionService extends CrudService<Transaction,String> {
    void clear();
    void save(Set<Transaction> transactions);
    BigDecimal getValueOfTransactionsBySender(String sender);
    List<TransactionOutput> getTransactionOutputsFromLastTransactionBySender(String sender);
}
