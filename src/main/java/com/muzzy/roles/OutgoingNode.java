package com.muzzy.roles;


import com.muzzy.net.TransactionSocketDto;
import com.muzzy.net.connection.OutgoingConnectionsRepository;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOG = LoggerFactory.getLogger(OutgoingNode.class);
    private final OutgoingConnectionsRepository outgoingConnectionsRepository;

    public OutgoingNode(OutgoingConnectionsRepository outgoingConnectionsRepository) {
        this.outgoingConnectionsRepository = outgoingConnectionsRepository;
    }

    /**
     * Send object to all connected nodes
     *
     * run() not need now
     */
    public void send() {
        //Blokujące!

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
                LOG.error("Can't send information");
                e.printStackTrace();
            }

        });
    }

    @Override
    public void run() {

    }
}
