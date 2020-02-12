package com.muzzy.service.map;

import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.Block;
import com.muzzy.domain.Transaction;
import com.muzzy.service.BlockService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

@Service
public class BlockMapService extends AbstractBlockMapService<Block, String> implements BlockService {


    @Override
    public LinkedHashSet<Block> getAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(String id) {
        super.deleteById(id);
    }

    @Override
    public void delete(Block t) {
        super.delete(t);
    }

    @Override
    public Block save(Block t) {
        if (t.getTransactions().isEmpty()) {
            return null;
        } else return super.save(t);
    }

    @Override
    public Block getById(String id) {
        return super.findById(id);
    }

    @Override
    public Block getLastBlock() {
        return super.getLastBlock();
    }

    @Override
    public Transaction getTransactionFromBlockById(String id) {
        if (id != null) {
            return this.getAll().stream().map(block -> block.getTransactions().stream()
                    .filter(transaction -> id.equals(transaction.getTransactionId()))
                    .findFirst()
                    .orElse(null)).filter(Objects::nonNull).findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public Block getBlockWithTransaction(String id) {
        if (id != null) {
//            this.getAll().stream().forEach(block -> block.getTransactions().stream().filter());
            for (Block block : this.getAll()) {
                for (Transaction transaction : block.getTransactions()
                ) {
                    if (id.equals(transaction.getTransactionId())) {
                        return block;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getChainHash() {
        List<Block> list = new ArrayList<>();
        list.addAll(findAll());
        StringBuilder string = new StringBuilder();
        list.stream().forEach(x -> string.append(x.getHash()));
        return StringUtil.applySha256(string.toString());
    }
}
