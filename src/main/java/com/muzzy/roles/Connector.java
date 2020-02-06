package com.muzzy.roles;

import com.muzzy.configuration.ConfigLoader;
import com.muzzy.net.connection.OutgoingConnectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> toConnection = configLoader.getAddresses();
        List<String> oC = outgoingConnectionsRepository.getSockets().stream().map(x -> x.getLocalAddress().getHostAddress()).collect(Collectors.toList());
        toConnection.removeIf(a -> check(oC,a));

        toConnection.forEach(address -> {
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

    public boolean check(List<String> addresses, String addr) {
        for (String address : addresses) {
            if (addr.equals(address)) {
                return true;
            }
        } return false;
    }
}