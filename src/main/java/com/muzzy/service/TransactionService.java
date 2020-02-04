package com.muzzy.service;

import com.muzzy.domain.Transaction;
import org.springframework.stereotype.Service;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */
@Service
public interface TransactionService extends CrudService<Transaction,String> {
    void clear();
}
