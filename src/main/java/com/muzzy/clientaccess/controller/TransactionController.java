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
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class TransactionController {
    final static Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final BlockMapService blockMapService;
    private final WalletMapService walletMapService;
    private final TransactionFactory transactionFactory;
    private final TransactionService transactionService;
    private final TransactionValidator transactionValidator;

    public TransactionController(BlockMapService blockMapService,
                                 WalletMapService walletMapService,
                                 TransactionFactory transactionFactory,
                                 TransactionService transactionService,
                                 TransactionValidator transactionValidator) {
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

    @ModelAttribute("transactionDto")
    public TransactionDto newTransaction() {
        return new TransactionDto();
    }

    @RequestMapping(value = "/transactions/getForm", method = RequestMethod.GET)
    public String getNewTransactionForm(@RequestParam String id, Model model,
                                        @ModelAttribute("transactionDto") final TransactionDto transactionDto) {

        Wallet sender = walletMapService.getById(id);
        List<String> allExceptId = walletMapService.getAllExceptId(sender.getPublicKey()).stream()
                .map(wallet -> wallet.getStringFromPubKey())
                .collect(Collectors.toList());

        model.addAttribute("receivers", allExceptId);
        model.addAttribute("transactionDto", transactionDto.setSender(id));

        LOG.debug("Wallet PublicKey: " + sender.getStringFromPubKey());
        return "transaction/add";
    }

    @RequestMapping(value = "/transactions/add", method = RequestMethod.POST)
    String addTransaction(@Valid @ModelAttribute("transactionDto") final TransactionDto transactionDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            Wallet sender = walletMapService.getById(transactionDto.getSender());
            List<String> allExceptId = walletMapService.getAllExceptId(sender.getPublicKey()).stream()
                    .map(wallet -> wallet.getStringFromPubKey())
                    .collect(Collectors.toList());
            model.addAttribute("receivers",allExceptId);
            return "transaction/add";
        }

        Wallet sender_wallet = walletMapService.getById(transactionDto.getSender());
        Wallet receiver_wallet = walletMapService.getById(transactionDto.getReceiver());

        Transaction transaction = transactionFactory.getTransaction(
                sender_wallet.getPrivateKey(),
                sender_wallet.getPublicKey(),
                receiver_wallet.getPublicKey(),
                transactionDto.getValue());

        LOG.debug(transactionDto.getSender());
        LOG.debug(transactionDto.getReceiver());
        LOG.debug(transactionDto.getValue().toString());
        LOG.warn("Ready to add transaction!");

        transactionService.save(transaction);
        return "redirect:/index";
    }

}
