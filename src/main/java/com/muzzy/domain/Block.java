package com.muzzy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.muzzy.Main;
import com.muzzy.cipher.StringUtil;
import com.muzzy.service.controllerservice.Validation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Getter
@Setter
@NoArgsConstructor

public abstract class Block implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(Block.class);

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime timestamp;
    private Long nonce = 0L;
    //    private List<Transaction> transactions = new ArrayList<>();
    private LinkedHashSet<Transaction> transactions = new LinkedHashSet<>();
    private String hash;
    private String previousHash;
    private Long hashingTime;
    private int difficulty;
    private String hashRoot;
    private double nodeId = Main.nodeId;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = ZonedDateTime.now();  // TODO: 2020-01-28 Poprawić nadawanie strefy czasowej
    }
    /**
     * Method can hash block with given difficulty
     * @param difficulty
     */
    public void mine(int difficulty) {
        this.hashRoot = StringUtil.getHashRoot(transactions);
        this.difficulty = difficulty;
        String toHash = this.previousHash  + "/" + this.timestamp.toEpochSecond() + "/" + this.hashRoot + "/" + difficulty;
        LOG.debug(toHash);
        do {
            this.nonce ++;
            this.hash = StringUtil.applySha256(toHash + this.nonce);
        } while (!hash.substring(0, difficulty).matches("[0]{" + difficulty + "}"));
    }

    public void addTransaction(Transaction transaction) {
        if(transaction == null) { return; }
        // TODO: 2020-01-28 To weryfikuje jedynie czy transakcja nie jest null, nie sprawdza czy wszytstko jest ustawione poprawnie, refactor
        String msg = transaction.getSender() + transaction.getReceiver() + transaction.getValue();
        boolean verifySignature = Validation.verifySignature(transaction.getSender(), msg, transaction.getSignature());
        if(verifySignature) {
            transactions.add(transaction);
        }
    }
    public Transaction getTransactionById(String hash){
        return transactions.stream().filter(t -> t.getTransactionId().equals(hash)).findFirst().orElse(null);
    }
}
