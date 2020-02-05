package com.muzzy.roles;


import com.muzzy.net.connection.OutgoingConnectionsRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Getter
@Setter
@Component
public class OutgoingNode implements Runnable {
    private Scanner scanner = new Scanner(System.in);

    private final Connector connector;
    private final OutgoingConnectionsRepository outgoingConnectionsRepository;

    public OutgoingNode(Connector connector, OutgoingConnectionsRepository outgoingConnectionsRepository) {
        this.connector = connector;
        this.outgoingConnectionsRepository = outgoingConnectionsRepository;
    }

    @Override
    public void run() {
//        int s;
//        do {
//            System.out.println("1. Test connection with servers");
//            s = scanner.nextInt();
//        } while (s < 1 && s > 2);
//
//        switch (s) {
//            case 1: {
//                System.out.println("Running test...");
//                connectedSockets = connector.connect();
//                break;
//            }
//        }
         connector.connect();
         outgoingConnectionsRepository.getSockets().forEach();
    }
}
