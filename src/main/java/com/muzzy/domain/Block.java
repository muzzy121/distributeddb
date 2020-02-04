package com.muzzy.domain;

import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.spsfl.SerialVersionUIDContainer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Getter
@Setter
@NoArgsConstructor
public abstract class Block implements Serializable {
    private static final long serialVersionUID = SerialVersionUIDContainer.BLOCK_SVUID;

//    private Long Id;
    private ZonedDateTime timestamp;
    private Long nonce = 0L;
    private List<Transaction> transactions = new ArrayList<>();
    private String hash;
    private String previousHash;
    private Long hashTime;
//    private long generatingTime;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = ZonedDateTime.now();  // TODO: 2020-01-28 Poprawić nadawanie strefy czasowej
    }

    /**
     * Method can hash block with given difficulty
     * @param difficulty
     */
    public void mine(int difficulty) {
        String toHash = this.previousHash + this.timestamp + this.transactions;
        do {
            this.nonce ++;
            this.hash = StringUtil.applySha256(toHash + this.nonce);
        } while (!hash.substring(0, difficulty).matches("[0]{" + difficulty + "}"));
    }

    public void addTransaction(Transaction transaction) {
        if(transaction == null) return;  // TODO: 2020-01-28 To weryfikuje jedynie czy transakcja nie jest null, nie sprawdza czy wszytstko jest ustawione poprawnie, refactor
        // TODO: 2020-01-28 Dlaczego sprawdzam czy poprzedni hash jest 0 ? Co to zmienia?
//        if((!previousHash.equals("0"))) {
//            if((!transaction.processTransaction())) {
//                System.out.println("Transaction failed to process. Discarded.");
//                return;
//            }
//        }
        transactions.add(transaction);
    }
    public Transaction getTransactionById(String hash){
        return transactions.stream().filter(t -> t.getTransactionId().equals(hash)).findFirst().orElse(null);
    }
}
