package com.muzzy.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Getter
@Setter
public abstract class Block {
    private Long id;
    private Long parentId;
    private String name;

}
