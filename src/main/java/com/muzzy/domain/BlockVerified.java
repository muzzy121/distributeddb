package com.muzzy.domain;

import com.muzzy.domain.spsfl.SerialVersionUIDContainer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BlockVerified extends Block {
    private static final long serialVersionUID = SerialVersionUIDContainer.BLOCK_VERIFIED_SVUID;

    public BlockVerified(String previousHash) {

        super(previousHash);
    }
}
