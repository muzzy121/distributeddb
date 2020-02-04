package com.muzzy.service.map;

import com.muzzy.domain.Transaction;
import com.muzzy.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Service
public class TransactionMapService extends AbstractTransactionMapService<Transaction, String> implements TransactionService {
    @Override
    public Set<Transaction> getAll() {
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
}
