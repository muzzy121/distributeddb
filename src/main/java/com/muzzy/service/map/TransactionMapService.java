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
public class TransactionMapService extends AbstractTransactionMapService<Transaction, Long> implements TransactionService {
    @Override
    public Set<Transaction> getAll() {
        return super.findAll();
    }

    @Override
    public Transaction getById(Long id) {
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
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
