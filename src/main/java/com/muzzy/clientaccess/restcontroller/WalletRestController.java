package com.muzzy.clientaccess.restcontroller;

import com.muzzy.domain.Wallet;
import com.muzzy.service.map.WalletMapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("api/wallet")
public class WalletRestController {
    private final WalletMapService walletMapService;

    public WalletRestController(WalletMapService walletMapService) {
        this.walletMapService = walletMapService;
    }

    @GetMapping("/all")
    public Set<Wallet> getWallets() {
        Set<Wallet> walletSet = new HashSet<>();
        walletSet.addAll(walletMapService.getAll());
        return walletSet;
    }
}
