package com.muzzy.clientaccess.restcontroller;

import com.muzzy.domain.Transaction;
import com.muzzy.domain.Wallet;
import com.muzzy.dto.TransactionDto;
import com.muzzy.dto.TransactionDtoPublic;
import com.muzzy.dto.WalletDto;
import com.muzzy.service.factory.TransactionFactory;
import com.muzzy.service.map.TransactionMapService;
import com.muzzy.service.map.TransactionOutputMapService;
import com.muzzy.service.map.WalletMapService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("sim")
// @AllArgsConstructor
public class SimulationController {

    private static final String JSON = "application/json"; // żeby nie pisać "consumes =" every fkn time
    private final WalletMapService walletMapService;  // final żeby się nie dało konstruować bezargumentowo
    private final TransactionMapService transactionMapService;
    private final TransactionOutputMapService transactionOutputMapService;
    private final TransactionFactory transactionFactory;

    public SimulationController(
            WalletMapService walletMapService,
            TransactionMapService transactionMapService,
            TransactionOutputMapService transactionOutputMapService,
            TransactionFactory transactionFactory) {
        this.walletMapService = walletMapService;
        this.transactionMapService = transactionMapService;
        this.transactionOutputMapService = transactionOutputMapService;
        this.transactionFactory = transactionFactory;
    }

    @GetMapping("/wallets")
    public Set<WalletDto> getAllWalletData() {

        Set<String> walletKeySet = walletMapService.getAllKeys();
        Set<WalletDto> walletDtoSet = new HashSet<>();
        for (String key :
                walletKeySet) {
            WalletDto walletDto = new WalletDto();
            walletDto.setPublicKey(key);
            walletDto.setBalance(transactionOutputMapService.getBalance(walletMapService.getById(key).getPublicKey()));
            walletDtoSet.add(walletDto);
        }
        return walletDtoSet;
    }

    @PostMapping(path = "/transaction", consumes = JSON)
    public void addTransaction(@RequestBody TransactionDtoPublic transactionDtoPublic) {
        Wallet senderWallet = walletMapService.getById(transactionDtoPublic.getSender());
        Wallet receiverWallet = walletMapService.getById(transactionDtoPublic.getReciever());
        Transaction transaction = transactionFactory.getTransaction(
                senderWallet.getPrivateKey(),
                senderWallet.getPublicKey(),
                receiverWallet.getPublicKey(),
                transactionDtoPublic.getValue());
        transactionMapService.save(transaction);
    }

    @PostMapping(path = "/wallet")
    public void generateWallets() {
        generateWallets(1);
    }

    @PostMapping(path = "/wallet/{i}")
    public void generateWallets(@PathVariable("i") int numberOfWallets) {
        for (int i = 0; i < numberOfWallets; i++) {
            Wallet wallet = new Wallet();
            walletMapService.save(wallet);
        }
    }
}
