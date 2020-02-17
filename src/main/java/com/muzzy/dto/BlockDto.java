package com.muzzy.dto;

import com.muzzy.domain.Transaction;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockDto {
    private String hash;
    private String previousHash;
    private long nonce;
    private Set<Transaction> transactions = new HashSet<>();
    private ZonedDateTime timestamp;
}
