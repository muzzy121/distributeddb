package com.muzzy.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class TransactionDto {
    private String sender;
    private String privateKey;
    private String reciever;
    private Float value;
}
