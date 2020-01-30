package com.muzzy.clientaccess.controller;

import com.muzzy.domain.Block;
import com.muzzy.domain.Transaction;
import com.muzzy.service.map.BlockMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TransactionController {
    final static Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final BlockMapService blockMapService;

    public TransactionController(BlockMapService blockMapService) {
        this.blockMapService = blockMapService;
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
}
