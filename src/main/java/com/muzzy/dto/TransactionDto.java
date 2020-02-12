package com.muzzy.dto;

import org.springframework.stereotype.Component;


@Component
public class TransactionDto extends TransactionDtoPublic {
    private String privateKey;

    public String getPrivateKey() {
        return privateKey;
    }

    public TransactionDto setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

}