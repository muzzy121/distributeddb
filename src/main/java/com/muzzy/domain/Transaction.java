package com.muzzy.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
    private Long id;
    private Client from;
    private Client where;

}
