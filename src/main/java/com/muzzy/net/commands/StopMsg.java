package com.muzzy.net.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class StopMsg implements Serializable {
    private String secret;

}
