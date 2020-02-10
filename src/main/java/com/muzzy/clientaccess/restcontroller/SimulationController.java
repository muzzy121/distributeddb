package com.muzzy.clientaccess.restcontroller;

import com.muzzy.domain.Transaction;
import com.muzzy.domain.Wallet;
import com.muzzy.dto.TransactionDto;
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
@AllArgsConstructor
public class SimulationController {

    private static final String JSON = "application/json";
    private WalletMapService walletMapService;
    private TransactionMapService transactionMapService;
    private TransactionOutputMapService transactionOutputMapService;
    private TransactionFactory transactionFactory;

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
    public void addTransaction(@RequestBody TransactionDto transactionDto) {
        Wallet senderWallet = walletMapService.getById(transactionDto.getSender());
        Wallet recieverWallet = walletMapService.getById(transactionDto.getReceiver());
        Transaction transaction = transactionFactory.getTransaction(
                senderWallet.getPrivateKey(),
                senderWallet.getPublicKey(),
                recieverWallet.getPublicKey(),
                transactionDto.getValue());
        transactionMapService.save(transaction);
    }

    @PostMapping(path = "/wallet/{i}")
    public void generateWallets(@PathVariable("i") int numberOfWallets) {
        for (int i = 0; i < numberOfWallets; i++) {
            Wallet wallet = new Wallet();
            walletMapService.save(wallet);
        }
    }
}
