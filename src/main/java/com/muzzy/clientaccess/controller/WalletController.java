package com.muzzy.clientaccess.controller;

import com.muzzy.domain.Wallet;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.map.WalletMapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class WalletController {
    private final WalletMapService walletMapService;
    private final TransactionOutputService transactionOutputService;

    public WalletController(WalletMapService walletMapService, TransactionOutputService transactionOutputService) {
        this.walletMapService = walletMapService;
        this.transactionOutputService = transactionOutputService;
    }

    @RequestMapping(value = "/wallets", method = RequestMethod.GET)
    public String getWallets(Model model){
        model.addAttribute("wallets", walletMapService.getAll());
        return "wallet/index";
    }
    @RequestMapping(value = "/wallets/detail", method = RequestMethod.POST)
    public String getWalletDetail(@RequestParam String id, Model model){
        Wallet wallet = walletMapService.getById(id);
        float balance = transactionOutputService.getBalance(wallet.getPublicKey());
        model.addAttribute("wallet", wallet);
        model.addAttribute("balance", balance);
        return "wallet/detail";
    }
}
