package com.muzzy.roles;

import com.muzzy.configuration.ConfigLoader;
import com.muzzy.net.connection.OutgoingConnectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

@Component
public class Connector {
    private ConfigLoader configLoader;
    private Socket socket;
    private final OutgoingConnectionsRepository outgoingConnectionsRepository;

    @Autowired
    public Connector(ConfigLoader configLoader, OutgoingConnectionsRepository outgoingConnectionsRepository) {
        this.configLoader = configLoader;
        this.outgoingConnectionsRepository = outgoingConnectionsRepository;
    }

    public void connect(){
            configLoader.getAddresses().forEach(address -> {
                System.out.println("Looking up for: " + address);
                InetSocketAddress inetSocketAddress = new InetSocketAddress(address, configLoader.getPort());
            try {
                socket = new Socket();
                socket.connect(inetSocketAddress);
                if(socket.isConnected()) {
                    outgoingConnectionsRepository.addSocket(socket);
                }
            } catch (IOException e) {
                System.out.println("Unable to connect with: " + address);
//                e.printStackTrace();
            }
        });
    }
    public void disconnect(){
        outgoingConnectionsRepository.clear();
    }
}
