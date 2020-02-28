package com.muzzy.clientaccess.restcontroller;

import com.muzzy.domain.Transaction;
import com.muzzy.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/chain")
public class TransactionRestController {
    private final String JSON = "application/json";
    private final TransactionService transactionService;


    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/transaction/all", method = RequestMethod.GET)
    public Set<Transaction> getTransactions() {
        return transactionService.getAll();
    }
    @RequestMapping(value = "/transaction/all/{id}", method = RequestMethod.GET)
    public Transaction getTransaction(@PathVariable String id) {
        return transactionService.getById(id);
    }

    @RequestMapping(value = "/transaction/add", method = RequestMethod.POST, consumes = JSON)
    public void addTransaction(@RequestBody Transaction transaction){
        transactionService.save(transaction);
    }
    @RequestMapping(value = "/transactions/add", method = RequestMethod.POST, consumes = JSON)
    public void addTransactions(@RequestBody Set<Transaction> transactions){
        transactionService.save(transactions);
    }
}
