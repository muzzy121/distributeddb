package com.muzzy.roles;

import com.muzzy.configuration.ConfigLoader;
import com.muzzy.net.connection.OutgoingConnectionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Connector {
    private ConfigLoader configLoader;
    private Socket socket;
    private final Logger LOG = LoggerFactory.getLogger(Connector.class);
    private final OutgoingConnectionsRepository outgoingConnectionsRepository;

    @Autowired
    public Connector(ConfigLoader configLoader, OutgoingConnectionsRepository outgoingConnectionsRepository) {
        this.configLoader = configLoader;
        this.outgoingConnectionsRepository = outgoingConnectionsRepository;
    }

    /**
     * Get IP list, try to connect to every one, except this which are connected by now
     * Collect all sockets to Outgoing Connections Repository
     */
    public void connect(){
        List<String> toConnection = configLoader.getAddresses();
        List<String> oC = outgoingConnectionsRepository.getSockets().stream().map(x -> x.getLocalAddress().getHostAddress()).collect(Collectors.toList());
        toConnection.removeIf(a -> check(oC,a));

        toConnection.forEach(address -> {
            LOG.debug("Looking up for: " + address);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(address, configLoader.getPort());
            try {
                socket = new Socket();
                socket.connect(inetSocketAddress);
                if(socket.isConnected()) {
                    outgoingConnectionsRepository.addSocket(socket);
                }
            } catch (IOException e) {
                LOG.debug("Unable to connect with: " + address);
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