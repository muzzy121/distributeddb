package com.muzzy.roles;

import com.muzzy.configuration.ConfigLoader;
import com.muzzy.net.Sendable;
import com.muzzy.net.connection.IncomingConnectionsRepository;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

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
@Setter
public class IncomingNode implements Runnable {

    private boolean isStart = true;
    private final Logger LOG = LoggerFactory.getLogger(IncomingNode.class);
    private final String CODE = "secret_code";
    private Socket socket;
    private ObjectInputStream objectInputStream;

    private final IncomingConnectionsRepository incomingConnectionsRepository;
    private final ConfigLoader configLoader;

    public IncomingNode(IncomingConnectionsRepository incomingConnectionsRepository, ConfigLoader configLoader) {

        this.incomingConnectionsRepository = incomingConnectionsRepository;
        this.configLoader = configLoader;
    }

    @Override
    public void run() {
        LOG.info("Incoming connection from: " + socket.getLocalSocketAddress());
        incomingConnectionsRepository.addSocket(socket);
    }

    private Object request(){
        try {
            InputStream inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

        } catch (IOException e) {
            LOG.error("Unable to get input stream");
            e.printStackTrace();
        }
        while (isStart) {
            try {
                Sendable object = (Sendable) objectInputStream.readObject();
                LOG.info("Received Transaction " + object.getClass() + " , ID: " + object.getId());
                return object;
            } catch (IOException e) {
//                e.printStackTrace();
                incomingConnectionsRepository.deleteSocket(socket);
                isStart=false;
                LOG.error("Disconnected");
            } catch (ClassNotFoundException e) {
                LOG.error("Unknown object");
//                e.printStackTrace();
            }
        } return null;
    }
    private void response() throws IOException {
        LOG.info("Sending blockchain...");
        //Send Im Node!

        //Send Block List in Network
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);


    }
}
