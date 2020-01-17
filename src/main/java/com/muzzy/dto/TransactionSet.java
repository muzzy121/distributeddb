package com.muzzy.dto;

import com.muzzy.domain.Transaction;
import com.muzzy.roles.Connector;
import com.muzzy.service.TransactionService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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


    private Set<Transaction> transactions;
    private ObjectOutputStream objectOutputStream;

    public TransactionSet() {
        this.transactions = new HashSet<>();
    }

    public void sendAllTransaction() {
        transactions.addAll(transactionService.getAll());

        Set<Socket> socketSet = connector.connect();
        socketSet.forEach(socket -> {
            try {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                objectOutputStream.writeObject(transactions);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    ;


}
