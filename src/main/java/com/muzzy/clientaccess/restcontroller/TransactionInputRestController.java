package com.muzzy.clientaccess.restcontroller;

import com.muzzy.domain.Transaction;
import com.muzzy.domain.Wallet;
import com.muzzy.dto.TransactionDto;
import com.muzzy.service.factory.TransactionFactory;
import com.muzzy.service.map.TransactionMapService;
import com.muzzy.service.map.WalletMapService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("bank")
@AllArgsConstructor
public class TransactionInputRestController {

    private static final String JSON = "application/json";
    private WalletMapService walletMapService;
    private TransactionMapService transactionMapService;
    private TransactionFactory transactionFactory;
    
    @GetMapping("/wallets")
    public Set<String> getAllPublicKeys(){
        return walletMapService.getAllKeys();
    }

    @PostMapping(path = "/transaction", consumes = JSON)
    public void addTransaction(@RequestBody TransactionDto transactionDto){
        Wallet senderWallet = walletMapService.getById(transactionDto.getSender());
        Wallet recieverWallet = walletMapService.getById(transactionDto.getReciever());
        Transaction transaction = transactionFactory.getTransaction(
                senderWallet.getPrivateKey(),
                senderWallet.getPublicKey(),
                recieverWallet.getPublicKey(),
                transactionDto.getValue());
        transactionMapService.save(transaction);
    }

    @PostMapping(path = "/wallet", consumes = JSON)
    public void addWallet(){

    }
}
