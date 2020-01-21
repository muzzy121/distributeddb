package com.muzzy.domain;

import com.muzzy.cipher.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Getter
@Setter
@NoArgsConstructor
public abstract class Block implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long Id;
    private ZonedDateTime timestamp;
    private Long nonce = 0L;
    private List<Transaction> transactions;
    private String hash;
    private String previousHash;
//    private long generatingTime;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = ZonedDateTime.now();  // NAPRAWIC STREFE CZASOWA
    }

    /**
     * Method can hash block with given difficulty
     *
     * @param difficulty
     */
    public void mine(int difficulty) {
        String toHash = this.previousHash + this.timestamp + this.transactions;
        do {
            this.nonce ++;
            this.hash = StringUtil.applySha256(toHash + this.nonce);
        } while (!hash.substring(0, difficulty).matches("[0]{" + difficulty + "}"));
    }


    //Method depends on some map, look it should be in other place!


//    public void addTransaction(Transaction transaction, HashMap<String, TransactionOutput> map) {
//        if(transaction == null) return;
//        if((!previousHash.equals("0"))) {
//            if((!transaction.processTransaction(map))) {
//                System.out.println("Transaction failed to process. Discarded.");
//                return;
//            }
//        }
//        System.out.println("\n"+transaction.sender.toString().substring(40,194)+"\nis Attempting to send funds ("+transaction.value+") to "+"\n"+transaction.receiver.toString().substring(40,194)+"\n...");
//        transactions.add(transaction);
//        System.out.println("Transaction Successfully added to Block");
//    }
}
