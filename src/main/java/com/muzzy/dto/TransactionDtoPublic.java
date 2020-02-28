package com.muzzy.dto;


import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Object that is beeing sent through REST
 */


@Component
public class TransactionDtoPublic {
    private String sender;
    private String receiver;
    private BigDecimal value;

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

    public BigDecimal getValue() {
        return value;
    }

    public TransactionDtoPublic setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

}
