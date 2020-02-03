package com.muzzy.clientaccess.restcontroller;

import com.muzzy.service.map.WalletMapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("elvis")  // return to sender... i gave a letter to the postman...
public class TransactionInputRestController {

    private WalletMapService walletMapService;

    public TransactionInputRestController(WalletMapService walletMapService) {
        this.walletMapService = walletMapService;
    }

    @GetMapping("/wallets")
    public Set<String> getAllPublicKeys(){
        return walletMapService.getAllKeys();
    }
}
