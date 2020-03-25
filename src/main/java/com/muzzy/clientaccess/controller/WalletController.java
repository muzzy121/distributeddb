package com.muzzy.clientaccess.controller;

import com.muzzy.domain.Wallet;
import com.muzzy.net.api.RESTApiControl;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.TransactionService;
import com.muzzy.service.map.WalletMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Controller
public class WalletController {
    private static final Logger LOG = LoggerFactory.getLogger(WalletController.class);

    private final WalletMapService walletMapService;
    private final TransactionOutputService transactionOutputService;
    private final TransactionService transactionService;
    private final RESTApiControl restApiControl;

    public WalletController(WalletMapService walletMapService, TransactionOutputService transactionOutputService, TransactionService transactionService, RESTApiControl restApiControl) {
        this.walletMapService = walletMapService;
        this.transactionOutputService = transactionOutputService;
        this.transactionService = transactionService;
        this.restApiControl = restApiControl;
    }

    @RequestMapping(value = "/wallets", method = RequestMethod.GET)
    public String getWallets(Model model) {
        Set<Wallet> walletsFromService = walletMapService.getAll();
        Set<Wallet> walletsFromNodes = new HashSet<>();
        walletsFromNodes.addAll(restApiControl.getWalletsFromNodes());

        if (!walletsFromNodes.isEmpty()) {
            walletsFromService.addAll(walletsFromNodes);
        }
        model.addAttribute("wallets", walletsFromService);
        return "wallet/index";
    }

    @RequestMapping(value = "/wallets/detail", method = RequestMethod.POST)
    public String getWalletDetail(@RequestParam(value = "id", required = false) String id, Model model) {
        if (id.equals(null) || id.equals("Choose...")) {
            return "redirect:/wallets";
        }

        LOG.debug(id);
        Wallet wallet = walletMapService.getById(id);
        BigDecimal balance = transactionOutputService.getBalance(wallet.getPublicKey()).subtract(transactionService.getValueOfTransactionsBySender(wallet.getPublicKey()));
        model.addAttribute("wallet", wallet);
        model.addAttribute("balance", balance);
        return "wallet/detail";
    }
}
