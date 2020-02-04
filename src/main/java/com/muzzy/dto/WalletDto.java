package com.muzzy.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class WalletDto {
    private String publicKey;
    private float balance;
}
