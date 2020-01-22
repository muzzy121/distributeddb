package com.muzzy.clientaccess.controller;

import com.muzzy.cipher.StringUtil;
import com.muzzy.dto.TransactionSet;
import com.muzzy.service.TransactionService;
import com.muzzy.service.controllerservice.test.RsaKeyGen;
import com.muzzy.service.controllerservice.test.Validation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        return "index";
    }
    @PostMapping("/add")
    public String doAdd(Model model){
//        transactionService.save(transaction);
//        transactionSet.sendAllTransaction();
        model.addAttribute("transactions", transactionService.getAll());
        return "index";
    }

    @GetMapping("/keys")
    public String KeyTest(Model model){
        model.addAttribute("pubKey", StringUtil.getStringFromKey(rsaKeyGen.getPubKey()));
        model.addAttribute("prvKey", StringUtil.getStringFromKey(rsaKeyGen.getPrvKey()));
        return "keys";
    }

    @RequestMapping(value = "/keys/submited", method= RequestMethod.POST)
    public String processForm(Model model, @RequestParam String pubKey) {
        String data = StringUtil.getStringFromKey(rsaKeyGen.getPubKey()) + pubKey;
        byte[] signature = Validation.confirm(rsaKeyGen.getPrvKey(), data);
        System.out.println("Signature: " + signature);
        System.out.println("Signature validation: " + Validation.verifySignature(rsaKeyGen.getPubKey(),data,signature));
        return "keys";
    }
}
