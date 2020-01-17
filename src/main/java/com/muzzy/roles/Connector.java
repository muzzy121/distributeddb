package com.muzzy.roles;

import com.muzzy.configuration.ConfigLoader;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope("prototype")
@NoArgsConstructor
public class Connector {

    private ConfigLoader configLoader;
    private Set<Socket> sockets;
    private Socket socket;

    @Autowired
    public Connector(ConfigLoader configLoader) {
        this.configLoader = configLoader;

    }

   public Set<Socket> connect(){
        sockets = new HashSet<>();

        configLoader.getAddresses().forEach(address -> {
                System.out.println("Looking up for: " + address);
                InetSocketAddress inetSocketAddress = new InetSocketAddress(address, configLoader.getPort());
            try {
                socket = new Socket();
                socket.connect(inetSocketAddress);
                sockets.add(socket);

            } catch (IOException e) {
                System.out.println("Unable to connect with: " + address);
//                e.printStackTrace();
            }
        });
        return null;
    }


}
