package com.muzzy.service.map;

import com.muzzy.domain.Transaction;
import com.muzzy.service.TransactionService;

import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */


public class TransactionMapService extends AbstractTransactionMapService<Transaction, Long> implements TransactionService {
    @Override
    public Set getBlocks() {
        return null;
    }

    @Override
    public Object getBlockById(Object o) {
        return null;
    }

    @Override
    public Object save(Object o) {
        return null;
    }

    @Override
    public void delete(Object o) {
    }

    @Override
    public void deleteById(Object o) {

    }
}
