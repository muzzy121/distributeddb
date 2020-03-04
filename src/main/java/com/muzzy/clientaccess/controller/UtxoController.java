package com.muzzy.clientaccess.controller;

import com.muzzy.dto.TransactionSet;
import com.muzzy.net.api.RESTApiControl;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.TransactionService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Scope("prototype")
public class UtxoController {
    private final TransactionOutputService transactionOutputService;
    private final RESTApiControl restApiControl;
    private final TransactionService transactionService;

    public UtxoController(TransactionOutputService transactionOutputService, TransactionSet transactionSet, RESTApiControl restApiControl, TransactionService transactionService) {
        this.transactionOutputService = transactionOutputService;
        this.restApiControl = restApiControl;
        this.transactionService = transactionService;
    }

    @GetMapping({"/", "index"})
    public String getIndexPage(Model model) {
//        transactionOutputService.save(restApiControl.getAllUtxo());

        model.addAttribute("transactions", transactionOutputService.getAll());
        model.addAttribute("transactionsToAdd", transactionService.getAll().isEmpty() ? null : transactionService.getAll());
        return "transaction/index";
    }
}
