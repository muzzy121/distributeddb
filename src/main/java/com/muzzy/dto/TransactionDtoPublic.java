package com.muzzy.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Object that is beeing setn though
 */

@Getter
@Setter
@Component
public class TransactionDtoPublic {
    private String sender;
    private String reciever;
    private Float value;

}
