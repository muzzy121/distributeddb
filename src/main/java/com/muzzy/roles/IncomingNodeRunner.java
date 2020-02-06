package com.muzzy.roles;

import com.muzzy.Main;
import com.muzzy.configuration.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class IncomingNodeRunner implements Callable<Integer> {
    private final Logger LOG = LoggerFactory.getLogger(Main.class);
    private Scanner scanner = new Scanner(System.in);
    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private Socket socket;


    private final ConfigLoader configLoader;
    private final ApplicationContext context;

    public IncomingNodeRunner(ConfigLoader configLoader, ApplicationContext context) {
        this.configLoader = configLoader;
        this.context = context;
    }

    @Override
    public Integer call() throws Exception {
        serverSocket = new ServerSocket(configLoader.getServerport());
        LOG.info("Ready for connection");
        while (true) {
            try {
//                socket = serverSocket.accept();
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            executorService = Executors.newSingleThreadExecutor();
            IncomingNode incomingNode = context.getBean(IncomingNode.class);
            incomingNode.setSocket(socket);
            executorService.submit(incomingNode);
            executorService.shutdown();
        }
    }
}
