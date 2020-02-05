package com.muzzy.net.connection;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Component
public class OutgoingConnectionsRepository {
    private Set<Socket> sockets = new HashSet<>();

    public void addSocket(Socket socket){
        sockets.add(socket);
    }
    public void deleteSocket(Socket socket){
        sockets.remove(socket);
    }

    public void clear() {
        sockets.clear();
    }
}
