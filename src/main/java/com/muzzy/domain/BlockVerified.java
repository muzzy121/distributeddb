package com.muzzy.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BlockVerified extends Block {

    public BlockVerified(String previousHash) {
        super(previousHash);
    }
}
