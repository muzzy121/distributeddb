package com.muzzy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Getter
@Setter
@NoArgsConstructor
public abstract class Block implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long Id;
    private Long timestamp;
    private Long magicNumber;
//    private List<Transaction> transactionList;
    private String message;
    private String previousHash;
    private String thisHash;
    private long generatingTime;

}
