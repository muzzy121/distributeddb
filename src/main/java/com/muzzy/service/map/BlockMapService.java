package com.muzzy.service.map;

import com.muzzy.domain.Block;
import com.muzzy.service.BlockService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class BlockMapService extends AbstractBlockMapService<Block,String> implements BlockService {
    @Override
    public Set<Block> getAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(String id) {}

    @Override
    public void delete(Block t) {}

    @Override
    public Block save(Block t) {
        return super.save(t);
    }

    @Override
    public Block getById(String id) {
        return null;
    }

    @Override
    public Block getLastBlock() {
        return super.getLastBlock();
    }
}
