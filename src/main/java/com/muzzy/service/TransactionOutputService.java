package com.muzzy.service;

import com.muzzy.domain.TransactionOutput;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */
@Service
public interface TransactionOutputService extends CrudService<TransactionOutput,String> {
    Set<TransactionOutput> getTransctionByReciever(String publicKey);
    BigDecimal getBalance(String publicKey);
    void clear();
    void save(Set<TransactionOutput> allUtxo);
    void save(ArrayList<TransactionOutput> allUtxo);
}
