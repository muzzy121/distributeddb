package com.muzzy.clientaccess.controller;

import com.muzzy.dto.TransactionSet;
import com.muzzy.net.api.RESTApiControl;
import com.muzzy.service.TransactionOutputService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Scope("prototype")
public class UtxoController {
    private final TransactionOutputService transactionOutputService;
    private final TransactionSet transactionSet;
    private final RESTApiControl restApiControl;

    public UtxoController(TransactionOutputService transactionOutputService, TransactionSet transactionSet, RESTApiControl restApiControl) {
        this.transactionOutputService = transactionOutputService;
        this.transactionSet = transactionSet;
        this.restApiControl = restApiControl;
    }


    @GetMapping({"/","index"})
    public String getIndexPage(Model model){
//        transactionOutputService.save(restApiControl.getAllUtxo());
        model.addAttribute("transactions", transactionOutputService.getAll());
        return "transaction/index";
    }
}
