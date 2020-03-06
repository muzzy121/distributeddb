package com.muzzy.clientaccess.controller;

import com.muzzy.domain.Block;
import com.muzzy.domain.Transaction;
import com.muzzy.dto.TransactionSet;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.map.BlockMapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlockchainController {
    private final TransactionOutputService transactionOutputService;
    private final BlockMapService blockMapService;
    private final TransactionSet transactionSet;

    public BlockchainController(TransactionOutputService transactionOutputService, TransactionSet transactionSet, BlockMapService blockMapService) {
        this.transactionOutputService = transactionOutputService;
        this.transactionSet = transactionSet;
        this.blockMapService = blockMapService;
    }

    @GetMapping({"/all"})
    public String getIndexPage(Model model) {
        model.addAttribute("chain", blockMapService.getAll());

        return "block/index";
    }

    @RequestMapping(value = {"/blocks/detail"}, method = RequestMethod.GET)
    public String getBlockInfo(@RequestParam("id") String id, Model model) {
        model.addAttribute("block", blockMapService.getById(id));
        return "block/detail";
    }

    @RequestMapping(value = {"/transactions/detail"}, method = RequestMethod.GET)
    public String getTransactionById(@RequestParam("blockid") String blockId, @RequestParam("id") String id, Model model) {
        Block block = blockMapService.getById(blockId);
        Transaction ts = block.getTransactionById(id);
        model.addAttribute("transactionInputs", ts.getInputs());
        model.addAttribute("transactionOutputs", ts.getOutputs());
        model.addAttribute("block", blockMapService.getById(blockId));
        model.addAttribute("transactionId", id);
        return "transaction/detail";
    }
}