package com.muzzy.clientaccess.restcontroller;

import com.muzzy.domain.TransactionOutput;
import com.muzzy.service.TransactionOutputService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class TransactionOutputRestController {
    private final TransactionOutputService transactionOutputService;

    public TransactionOutputRestController(TransactionOutputService transactionOutputService) {
        this.transactionOutputService = transactionOutputService;
    }

    public Set<TransactionOutput> getAll(){
        return transactionOutputService.getAll();
    }
}
