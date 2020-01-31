package com.muzzy.clientaccess.controller;

import com.muzzy.domain.Block;
import com.muzzy.domain.Transaction;
import com.muzzy.domain.Wallet;
import com.muzzy.service.map.BlockMapService;
import com.muzzy.service.map.WalletMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;


@Controller
public class TransactionController {
    final static Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final BlockMapService blockMapService;
    private final WalletMapService walletMapService;

    public TransactionController(BlockMapService blockMapService, WalletMapService walletMapService) {
        this.blockMapService = blockMapService;
        this.walletMapService = walletMapService;
    }

    @RequestMapping(value = {"/transactions"}, method = RequestMethod.GET)
    public String findTransaction(@RequestParam(name = "id") String id, Model model){
        if (id.trim() ==""){
            return "error/error";
        }
        String transactionId = id.trim();
        Transaction transaction = blockMapService.getTransactionFromBlockById(transactionId);
        Block block = blockMapService.getBlockWithTransaction(transactionId);
        LOG.debug(transaction.getSignature().toString());
        model.addAttribute("transactionInputs", transaction.getInputs());
        model.addAttribute("transactionOutputs", transaction.getOutputs());
        model.addAttribute("block", block);
        model.addAttribute("transactionId", transactionId);
        LOG.debug(block.getHash());
        LOG.debug(transactionId);
        return "transaction/detail";
    }
    @RequestMapping(value = "transactions/add", method = RequestMethod.POST)
    public String addTransaction(@RequestParam String id, Model model){
          Wallet sender = walletMapService.getById(id);
          Set<Wallet> recivers = walletMapService.getAll();
          Float value = 0F;
          model.addAttribute("sender", sender);
          model.addAttribute("recivers", recivers);
          model.addAttribute(value);
//        transactionService.save(transaction);
//        transactionSet.sendAllTransaction();
        return "transaction/add";
    }
}
