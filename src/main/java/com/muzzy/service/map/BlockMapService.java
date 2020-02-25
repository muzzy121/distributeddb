package com.muzzy.service.map;

import com.muzzy.domain.Block;
import com.muzzy.domain.BlockVerified;
import com.muzzy.domain.Transaction;
import com.muzzy.service.BlockService;
import com.muzzy.service.controllerservice.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BlockMapService extends AbstractBlockMapService<Block, String> implements BlockService {

    private static final Logger LOG = LoggerFactory.getLogger(BlockMapService.class);

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
    public Block save(LinkedHashSet<BlockVerified> blockLinkedHashSet) {
        if(blockLinkedHashSet == null){
            return null;
        }
        List<Block> blocks = blockLinkedHashSet.stream().collect(Collectors.toList());
//        blocks.addAll(blockLinkedHashSet);

        //Verify transfered blocks
        //Sum all

        List<Block> linkedHashSet = getAll().stream().collect(Collectors.toList());
        LOG.info("Old chain Valid: " + Validation.isChainValid(linkedHashSet).toString());
        LOG.info("New chain Valid: " + Validation.isChainValid(blocks));

        if(linkedHashSet.isEmpty()) {
            super.saveAll(blocks);
            return null;
        }

        if(linkedHashSet.get(linkedHashSet.size()-1).getHash() == blocks.get(0).getPreviousHash()) {
            LOG.info("Ready to add blocks");
            linkedHashSet.addAll(blocks);
        }
        return null;
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
    public LinkedHashSet<Block> getBlocksAfter(String id) {
        LinkedHashSet<Block> tempBlocks = getAll();
        LinkedHashSet<Block> temp = getAll();
        for (Block block : temp) {
            tempBlocks.remove(block);
            if (block.getHash().equals(id)) {
                break;
            }
        }
        return tempBlocks;
    }

//    public String getChainHash() {
//        List<Block> list = new ArrayList<>();
//        list.addAll(findAll());
//        StringBuilder string = new StringBuilder();
//        list.stream().forEach(x -> string.append(x.getHash()));
//        return StringUtil.applySha256(string.toString());
//    }
}
