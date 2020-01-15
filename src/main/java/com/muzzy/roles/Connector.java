package com.muzzy.roles;

import com.muzzy.configuration.ConfigLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connector {
    @Autowired
    private ConfigLoader configLoader;

    private Socket Connect(){
        configLoader.getAddresses().forEach(address -> {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(address, configLoader.getPort());
            try {
                Socket socket = new Socket().connect(inetSocketAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


}
