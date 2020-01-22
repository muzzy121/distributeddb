package com.muzzy.service.map;

import com.muzzy.domain.Transaction;
import com.muzzy.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Service
public class TransactionMapService extends AbstractTransactionMapService<Transaction, String> implements TransactionService {
    @Override
    public Set<Transaction> getAll() {
        return super.findAll();
    }

    @Override
    public Transaction getById(String id) {
        return super.findById(id);
    }

    @Override
    public Transaction save(Transaction t) {

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

        return super.save(t);
    }

    @Override
    public void delete(Transaction t) {
        super.delete(t);
    }

    @Override
    public void deleteById(String id) {
        super.deleteById(id);
    }
}
