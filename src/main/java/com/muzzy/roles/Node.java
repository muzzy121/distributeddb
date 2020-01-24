package com.muzzy.roles;

import com.muzzy.configuration.ConfigLoader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.*;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */


// TODO: 2020-01-14 Przemyśleć jak moża tu skorzystać ze DI
// TODO: 2020-01-14 Kolejność operacji po starcie Node-a
// TODO: 2020-01-14 Jak powinny się komunikowac node'y i czy muszą tworzyć połączenie.

/**
 * 1. Scenariusz. Pinguje do wszystkich node'ów, jak odpowiadają to wywyłam baze i zamykam połączenie.
 * (COŚ NA ZASADZIE OFFLINE SYNC. Jak zachodzi zmiana w node, to operację wykonuje ponownie)
 * Narazie brak pomysłu jak tą synchronizację przeprowadzać.
 * Można robić timestamp w oparciu o jakieś UTC
 * <p>
 * Napewno serwery będą musiały miec jakiś status ( ready, busy, in-synced )
 * Wydaje się ze trzeba podzielić na dwa etapy. Po starcie:
 * Próbuje nawiązać połączenie z serwerami.
 */
@Component
@Scope("prototype")
@NoArgsConstructor
public class Node implements Runnable {
    private boolean isStart = true;
    private final String CODE = "secret_code";
    private Socket socket;
    private ObjectInputStream objectInputStream;

    @Autowired
    private ConfigLoader configLoader;

    public Node(Socket socket) {
        this.socket = socket;
    }

    public Node setSocket(Socket socket) {
        this.socket = socket;
        return this;
    }

    @Override
    public void run() {
//        System.out.println("Waiting for data...");
//        try {
//            InputStream inputStream = socket.getInputStream();
//            objectInputStream = new ObjectInputStream(inputStream);
//
//        } catch (IOException e) {
//            System.out.println("Unable to get input stream");
//            e.printStackTrace();
//        }
//
//        while (isStart) {
//            try {
//                Object object = objectInputStream.readObject();
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("Unknown object");
//            } catch (ClassNotFoundException e) {
//                System.out.println("Unknown object");
//                e.printStackTrace();
//            }
//        }
    }
}
