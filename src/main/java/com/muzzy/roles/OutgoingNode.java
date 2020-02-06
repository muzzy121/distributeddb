package com.muzzy.roles;


import com.muzzy.net.TransactionSocketDto;
import com.muzzy.net.connection.OutgoingConnectionsRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

@Getter
@Setter
@Component
public class OutgoingNode implements Runnable {
    private Scanner scanner = new Scanner(System.in);
    private ObjectOutputStream objectOutputStream;
    private final Connector connector;
    private final OutgoingConnectionsRepository outgoingConnectionsRepository;

    public OutgoingNode(Connector connector, OutgoingConnectionsRepository outgoingConnectionsRepository) {
        this.connector = connector;
        this.outgoingConnectionsRepository = outgoingConnectionsRepository;
    }

    @Override
    public void run() {

        connector.connect();
        //Wydaje mi sie ze tu operacja zostanie zablokowana. Teraz pytanie czy połaczenia nie powinny być załatwione wczesniej na osobnym wątku, a potem dopiero wysyłka na blokująco

        outgoingConnectionsRepository.getSockets().forEach(socket -> {
            if(objectOutputStream == null) {
                try {
                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }}
            try {
                objectOutputStream.writeObject(new TransactionSocketDto());
                objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}
