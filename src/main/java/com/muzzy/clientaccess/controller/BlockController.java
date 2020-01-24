package com.muzzy.clientaccess.controller;

import com.muzzy.dto.TransactionSet;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.controllerservice.test.RsaKeyGen;
import com.muzzy.service.map.BlockMapService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Scope("prototype")
public class BlockController {
    private final TransactionOutputService transactionOutputService;
    private final BlockMapService blockMapService;
    private final TransactionSet transactionSet;

    public BlockController(TransactionOutputService transactionOutputService, TransactionSet transactionSet, BlockMapService blockMapService) {
        this.transactionOutputService = transactionOutputService;
        this.transactionSet = transactionSet;
        this.blockMapService = blockMapService;
    }

    @GetMapping({"/blockall"})
    public String getIndexPage(Model model) {
        model.addAttribute("chain", blockMapService.getAll());

        return "block/index";
    }
    @RequestMapping(value = {"/block/details"}, method = RequestMethod.GET)
    public String getBlockInfo(@RequestParam("id") String id, Model model){
        model.addAttribute("block", blockMapService.getById(id));
        return "block/detail";
    }
    // Use for nothing
//    @PostMapping("/addBlock")
//    public String doAdd(Model model) {
////        transactionService.save(transaction);
////        transactionSet.sendAllTransaction();
//        model.addAttribute("transactions", transactionOutputService.getAll());
//        return "index";
//    }
}
