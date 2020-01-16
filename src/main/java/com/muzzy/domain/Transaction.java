package com.muzzy.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Getter
@Setter
public class Transaction implements Serializable {
    private Long id;
    private Client from;
    private Client where;

}
