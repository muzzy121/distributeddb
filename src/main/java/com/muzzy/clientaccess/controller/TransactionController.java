package com.muzzy.clientaccess.controller;

import com.muzzy.domain.Block;
import com.muzzy.domain.Transaction;
import com.muzzy.domain.Wallet;
import com.muzzy.dto.TransactionDto;
import com.muzzy.service.TransactionService;
import com.muzzy.service.factory.TransactionFactory;
import com.muzzy.service.map.BlockMapService;
import com.muzzy.service.map.WalletMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Set;


@Controller
public class TransactionController {
    final static Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final BlockMapService blockMapService;
    private final WalletMapService walletMapService;
    private final TransactionFactory transactionFactory;
    private final TransactionService transactionService;

    public TransactionController(BlockMapService blockMapService, WalletMapService walletMapService, TransactionFactory transactionFactory, TransactionService transactionService) {
        this.blockMapService = blockMapService;
        this.walletMapService = walletMapService;
        this.transactionFactory = transactionFactory;
        this.transactionService = transactionService;
    }

    @RequestMapping(value = {"/transactions"}, method = RequestMethod.GET)
    public String findTransaction(@RequestParam(name = "id") String id, Model model) {
        if (id.trim() == "") {
            return "error/error";
        }
        String transactionId = id.trim();
        Transaction transaction = blockMapService.getTransactionFromBlockById(transactionId);
        Block block = blockMapService.getBlockWithTransaction(transactionId);
        model.addAttribute("transactionInputs", transaction.getInputs());
        model.addAttribute("transactionOutputs", transaction.getOutputs());
        model.addAttribute("block", block);
        model.addAttribute("transactionId", transactionId);


        LOG.debug(transaction.getSignature().toString());
        LOG.debug(block.getHash());
        LOG.debug(transactionId);
        return "transaction/detail";
    }

    @RequestMapping(value = "/transactions/getForm", method = RequestMethod.POST)
    public String getNewTransactionForm(@RequestParam String id, Model model) {
        Wallet sender = walletMapService.getById(id);
        Set<Wallet> allExceptId = walletMapService.getAllExceptId(sender.getPublicKey());
        Float value = 0F;
        TransactionDto tdto = new TransactionDto();
        tdto.setSender(id);
        model.addAttribute("sender", sender.getStringFromPubKey());
        model.addAttribute("recivers", allExceptId);
        model.addAttribute(value);
        model.addAttribute("tdto", tdto);

//        transactionSet.sendAllTransaction();
        LOG.debug("Wallet PublicKey: " + sender.getStringFromPubKey());
//        LOG.debug(allExceptId.toString());
        return "transaction/add";
    }

    @RequestMapping(value = "/transactions/add", method = RequestMethod.POST)
    RedirectView addTransaction(@ModelAttribute TransactionDto tdto, Model model) {
        LOG.debug(tdto.getSender());
        LOG.debug(tdto.getReciever());
        LOG.debug(tdto.getValue().toString());
        LOG.warn("Ready to add transaction!");

        Wallet sender_wallet = walletMapService.getById(tdto.getSender());
        Wallet reciever_wallet = walletMapService.getById(tdto.getReciever());

        Transaction transaction = transactionFactory.getTransaction(sender_wallet.getPrivateKey(),sender_wallet.getPublicKey(),reciever_wallet.getPublicKey(),tdto.getValue());
        transactionService.save(transaction);
    return new RedirectView("/index");
    }
}
