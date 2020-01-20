package com.muzzy.clientaccess.controller;

import com.muzzy.domain.Client;
import com.muzzy.domain.Transaction;
import com.muzzy.dto.TransactionSet;
import com.muzzy.service.TransactionService;
import com.muzzy.service.controllerservice.test.RsaKeyGen;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Scope("prototype")
public class IndexController {
    private final TransactionService transactionService;
    private final TransactionSet transactionSet;
    private final RsaKeyGen rsaKeyGen;

    public IndexController(TransactionService transactionService, TransactionSet transactionSet, RsaKeyGen rsaKeyGen) {
        this.transactionService = transactionService;
        this.transactionSet = transactionSet;
        this.rsaKeyGen = rsaKeyGen;
    }

    @GetMapping({"/","index"})
    public String getIndexPage(Model model){
            model.addAttribute("transactions", transactionService.getAll());
            model.addAttribute("pubKey", rsaKeyGen.getPubKey());
            model.addAttribute("prvKey", rsaKeyGen.getPrvKey());

        return "index";
    }
    @PostMapping("/add")
    public String doAdd(Model model){
        Transaction transaction = new Transaction();
        transaction.setFrom(new Client().builder().id(4L).nickName("Janusz").build());
        transaction.setWhere(new Client().builder().id(3L).nickName("Andrzej").build());
        transactionService.save(transaction);
        transactionSet.sendAllTransaction();
        model.addAttribute("transactions", transactionService.getAll());
        return "index";
    }
}
