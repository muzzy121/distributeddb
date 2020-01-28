package com.muzzy.service.map;

import com.muzzy.domain.Wallet;
import com.muzzy.service.TransactionOutputService;
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
    @RequestMapping(value = "/wallet/detail", method = RequestMethod.POST)
    public String getWalletDetail(@RequestParam String selected_Wallet, Model model){
        Wallet wallet = walletMapService.getById(selected_Wallet);
        float balance = transactionOutputService.getBalance(wallet.getPublicKey());
        model.addAttribute("wallet", wallet);
        model.addAttribute("balance", balance);
        return "wallet/detail";
    }
}
