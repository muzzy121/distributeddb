package com.muzzy.roles;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@Component
@Scope("prototype")
public class TestThread implements Runnable {
    private Connector connector;
    private Set<Socket> connectedSockets = new HashSet<>();
    private Scanner scanner = new Scanner(System.in);

    @Autowired
    public TestThread(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void run() {
        int s;
        do {
            System.out.println("1. Test connection with servers");
            s = scanner.nextInt();
        } while (s < 1 && s > 2);

        switch (s) {
            case 1: {
                System.out.println("Running test...");
                connectedSockets = connector.connect();
                break;
            }
        }

    }
}
