package com.muzzy.service.controllerservice;

import com.muzzy.cipher.Code;
import com.muzzy.domain.Block;
import com.muzzy.domain.BlockUnverified;

public class BlockControllerService {

    public Block generateBlock(String message, int minZeros, String lastHash) {
        Block block = new BlockUnverified();
        block.setTimestamp(System.currentTimeMillis());
        block.setMessage(message);
        block.setPreviousHash(lastHash);
        mine(block,minZeros);
        return block;
    }

    public void mine(Block block, int minZeros) {
        boolean isMagic = false;
        int magicNumber = 0;
        String hash = null;
        long timeStart = System.currentTimeMillis() / 1000;
        while (!isMagic) {
            StringBuilder toHash = new StringBuilder();
            magicNumber = (int) (Math.random() * 10000000);
            toHash.append(block.getTimestamp()).append(block.getPreviousHash()).append(block.getMessage()).append(magicNumber);
            hash = Code.applySha256(String.valueOf(toHash));

            if (hash.substring(0, minZeros).matches("[0]{" + minZeros + "}")) {
                isMagic = true;
            }
        }
        block.setGeneratingTime(System.currentTimeMillis() / 1000 - timeStart);
        block.setMagicNumber((long) magicNumber);
        block.setThisHash(hash);
    }
}
