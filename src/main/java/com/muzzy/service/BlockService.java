package com.muzzy.service;

import com.muzzy.domain.Block;
import com.muzzy.domain.Transaction;
import org.springframework.stereotype.Service;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */
@Service
public interface BlockService extends CrudService<Block,String> {
    Block getLastBlock();
    Transaction getTransactionFromBlockById(String id);
    Block getBlockWithTransaction(String id);
    String getChainHash();
}
