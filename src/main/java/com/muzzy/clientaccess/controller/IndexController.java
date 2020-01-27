package com.muzzy.clientaccess.controller;

import com.muzzy.dto.TransactionSet;
import com.muzzy.service.TransactionOutputService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Scope("prototype")
public class IndexController {
    private final TransactionOutputService transactionOutputService;
    private final TransactionSet transactionSet;

    public IndexController(TransactionOutputService transactionOutputService, TransactionSet transactionSet) {
        this.transactionOutputService = transactionOutputService;
        this.transactionSet = transactionSet;
    }


    @GetMapping({"/","index"})
    public String getIndexPage(Model model){
            model.addAttribute("transactions", transactionOutputService.getAll());

        return "transaction/index";
    }


    @PostMapping("/add")
    public String doAdd(Model model){
//        transactionService.save(transaction);
//        transactionSet.sendAllTransaction();
        model.addAttribute("transactions", transactionOutputService.getAll());
        return "index";
    }

    @GetMapping("/keys")
    public String KeyTest(Model model){
//        model.addAttribute("pubKey", StringUtil.getStringFromKey(rsaKeyGen.getPubKey()));
//        model.addAttribute("prvKey", StringUtil.getStringFromKey(rsaKeyGen.getPrvKey()));
        return "keys";
    }

    @RequestMapping(value = "/keys/submited", method= RequestMethod.POST)
    public String processForm(Model model, @RequestParam String pubKey) {
//        String data = StringUtil.getStringFromKey(rsaKeyGen.getPubKey()) + pubKey;
//        byte[] signature = Validation.confirm(rsaKeyGen.getPrvKey(), data);
//        System.out.println("Signature: " + signature);
//        System.out.println("Signature validation: " + Validation.verifySignature(rsaKeyGen.getPubKey(),data,signature));
        return "keys";
    }
}
