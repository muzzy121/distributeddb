package com.muzzy.roles;

import com.muzzy.net.Sendable;
import com.muzzy.net.connection.IncomingConnectionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

import static com.muzzy.Main.isStart;
@Component
public class TestThread implements Callable<Integer> {
    private final Logger LOG = LoggerFactory.getLogger(TestThread.class);
    private final IncomingConnectionsRepository incomingConnectionsRepository;
    private Socket socket;
    private ObjectInputStream objectInputStream;

    public TestThread(IncomingConnectionsRepository incomingConnectionsRepository) {
        this.incomingConnectionsRepository = incomingConnectionsRepository;
    }

    @Override
    public Integer call() throws Exception {
        socket = incomingConnectionsRepository.getSockets().stream().findFirst().get();
        request();
        return null;
    }

    private Object request() {
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
                isStart = false;
                LOG.error("Disconnected");
            } catch (ClassNotFoundException e) {
                LOG.error("Unknown object");
//                e.printStackTrace();
            }
        }
        return null;
    }

    private void response() throws IOException {
        LOG.info("Sending blockchain...");
        //Send Im Node!

        //Send Block List in Network
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);


    }
}

