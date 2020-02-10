package com.muzzy.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class TransactionDto extends TransactionDtoPublic {
    private String privateKey;

    @Override
    public String getSender() {
        return super.getSender();
    }

    @Override
    public void setSender(String sender) {
        super.setSender(sender);
    }

    @Override
    public String getReciever() {
        return super.getReciever();
    }

    @Override
    public void setReciever(String reciever) {
        super.setReciever(reciever);
    }

    @Override
    public Float getValue() {
        return super.getValue();
    }

    @Override
    public void setValue(Float value) {
        super.setValue(value);
    }
}
