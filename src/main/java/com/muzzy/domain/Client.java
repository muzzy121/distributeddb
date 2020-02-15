package com.muzzy.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */
@Getter
@Setter
@NoArgsConstructor
public class Client implements Serializable {

    private long id;
    private String nickName;
    private String password;

    @Builder
    public Client(long id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }
}
