package com.muzzy.service;

import com.muzzy.domain.TransactionOutput;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */
@Service
public interface TransactionOutputService extends CrudService<TransactionOutput,String> {
    Set<TransactionOutput> getTransctionByReciever(PublicKey publicKey);
    float getBalance(PublicKey publicKey);
}
