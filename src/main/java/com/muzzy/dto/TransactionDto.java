package com.muzzy.dto;

import org.springframework.stereotype.Component;

@Component
public class TransactionDto {
    private String sender;
    private String privateKey;
    private String receiver;
    private Float value;

    public String getSender() {
        return sender;
    }

    public TransactionDto setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public TransactionDto setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public String getReceiver() {
        return receiver;
    }

    public TransactionDto setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public Float getValue() {
        return value;
    }

    public TransactionDto setValue(Float value) {
        this.value = value;
        return this;
    }
}
