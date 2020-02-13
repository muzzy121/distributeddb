package com.muzzy.dto;


import org.springframework.stereotype.Component;

/**
 * Object that is beeing sent through REST
 */


@Component
public class TransactionDtoPublic {
    public String getSender() {
        return sender;
    }

    public TransactionDtoPublic setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getReceiver() {
        return receiver;
    }

    public TransactionDtoPublic setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public Float getValue() {
        return value;
    }

    public TransactionDtoPublic setValue(Float value) {
        this.value = value;
        return this;
    }

    private String sender;
    private String receiver;
    private Float value;

}
