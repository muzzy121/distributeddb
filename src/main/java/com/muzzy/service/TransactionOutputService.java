package com.muzzy.service;

import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionOutput;
import org.springframework.stereotype.Service;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */
@Service
public interface TransactionOutputService extends CrudService<TransactionOutput,String> {
}
