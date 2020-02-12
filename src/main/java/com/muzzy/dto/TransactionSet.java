package com.muzzy.dto;

import com.muzzy.domain.Transaction;
import com.muzzy.roles.Connector;
import com.muzzy.roles.OutgoingNode;
import com.muzzy.service.TransactionService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Getter
@Setter
@Service
public class TransactionSet implements Serializable {
    @Autowired
    private Connector connector;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private OutgoingNode outgoingNode;

    private Set<Transaction> transactions;
    private ObjectOutputStream objectOutputStream;

    public TransactionSet() {
        this.transactions = new HashSet<>();
    }

    public void sendAllTransaction() {
        transactions.addAll(transactionService.getAll());

//        System.out.println("Before list");
//        socketSet.forEach(socket -> {
//            try {
//                System.out.println("After list");
//                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                objectOutputStream.writeObject(transactions);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
    }
}
