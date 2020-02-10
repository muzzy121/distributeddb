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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
public class TransactionController {
    final static Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final BlockMapService blockMapService;
    private final WalletMapService walletMapService;
    private final TransactionFactory transactionFactory;
    private final TransactionService transactionService;
    private final TransactionValidator transactionValidator;

    public TransactionController(BlockMapService blockMapService, WalletMapService walletMapService, TransactionFactory transactionFactory, TransactionService transactionService, TransactionValidator transactionValidator) {
        this.blockMapService = blockMapService;
        this.walletMapService = walletMapService;
        this.transactionFactory = transactionFactory;
        this.transactionService = transactionService;
        this.transactionValidator = transactionValidator;
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

    @InitBinder
    protected void initTransactionFormBinder(WebDataBinder binder) {
        binder.addValidators(transactionValidator);
    }

    @RequestMapping(value = "/transactions/getForm", method = RequestMethod.GET)
    public String getNewTransactionForm(@RequestParam String id, Model model ) {

        Wallet sender = walletMapService.getById(id);
        Set<String> allExceptId = walletMapService.getAllExceptId(sender.getPublicKey()).stream().map(wallet -> wallet.getStringFromPubKey()).collect(Collectors.toSet());


        model.addAttribute("sender", sender.getStringFromPubKey()); // send hashed pubkey of sender
        model.addAttribute("receivers", allExceptId); // send hashed pubkey of possible receivers
        model.addAttribute("tdto", new TransactionDto().setSender(id));

//        transactionSet.sendAllTransaction();
        LOG.debug("Wallet PublicKey: " + sender.getStringFromPubKey());
//        LOG.debug(allExceptId.toString());
        return "transaction/add";
    }

    @RequestMapping(value = "/transactions/add", method = RequestMethod.POST)
    String addTransaction(@Valid TransactionDto tdto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("id", tdto.getSender());
            return "redirect:/transactions/getForm";
        }

        if((tdto == null) || tdto.getSender().equals("") || tdto.getReceiver().equals("") || tdto.getValue() == null) {
            return "redirect:/wallets";
        }

        LOG.debug(tdto.getSender());
        LOG.debug(tdto.getReceiver());
        LOG.debug(tdto.getValue().toString());
        LOG.warn("Ready to add transaction!");
        Wallet sender_wallet = walletMapService.getById(tdto.getSender());
        Wallet reciever_wallet = walletMapService.getById(tdto.getReceiver());

        Transaction transaction = transactionFactory.getTransaction(sender_wallet.getPrivateKey(),sender_wallet.getPublicKey(),reciever_wallet.getPublicKey(),tdto.getValue());
        transactionService.save(transaction);
    return "redirect:/index";
    }

//    @RequestMapping(value = "/transactions/add", method = RequestMethod.POST)
//    RedirectView addTransaction(@ModelAttribute TransactionDto tdto, Model model) {
//        if((tdto == null) || tdto.getSender().equals("") || tdto.getReciever().equals("") || tdto.getValue() == null) {
//            return new RedirectView("/redirectTo");
//        }
//
//
//        LOG.debug(tdto.getSender());
//        LOG.debug(tdto.getReciever());
//        LOG.debug(tdto.getValue().toString());
//        LOG.warn("Ready to add transaction!");
//        Wallet sender_wallet = walletMapService.getById(tdto.getSender());
//        Wallet reciever_wallet = walletMapService.getById(tdto.getReciever());
//
//        Transaction transaction = transactionFactory.getTransaction(sender_wallet.getPrivateKey(),sender_wallet.getPublicKey(),reciever_wallet.getPublicKey(),tdto.getValue());
//        transactionService.save(transaction);
//    return new RedirectView("/index");
//    }


}
