package com.muzzy.clientaccess.restcontroller;

import com.muzzy.domain.TransactionOutput;
import com.muzzy.service.TransactionOutputService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/chain")
public class TransactionOutputRestController {
    private final TransactionOutputService transactionOutputService;

    public TransactionOutputRestController(TransactionOutputService transactionOutputService) {
        this.transactionOutputService = transactionOutputService;
    }
    @RequestMapping(value = "/utxo/all", method = RequestMethod.GET)
    public Set<TransactionOutput> getAll(){
        return transactionOutputService.getAll();
    }
}
