package com.muzzy.roles;

import java.io.IOException;
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
 * Szukam serwerów, pobieram listę
 */

public class Node implements Runnable {
    private boolean isStart = true;
    private DatagramSocket datagramSocket;
    private InetSocketAddress inetSocketAddress;
    private final String CODE = "secret_code";



    @Override
    public void run() {
//        try {
//            datagramSocket = new DatagramSocket(inetSocketAddress);
//            datagramSocket.setBroadcast(true);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        while (isStart) {
//            byte[] response = new byte[15000];  // create a response, which can handle packet ones will be get later in  datagramSocket.receive!
//            DatagramPacket toRecivePacket = new DatagramPacket(response, response.length);
//            try {
//                System.out.println("Waiting for packet!");
//                datagramSocket.receive(toRecivePacket);
//
//                System.out.println("Recived from: " + toRecivePacket.getAddress().getHostAddress() + ", " + toRecivePacket.getPort());
//
//                // TODO: 2019-11-07 See if I can catch broadcast packet!
//                // TODO: 2019-11-07 Check if packet is fine and send back packet
//                // TODO: 2019-11-07 In response send Servername!!!!
//                String stringFromBytes = new String(toRecivePacket.getData()).trim();
//                if (CODE.equals(stringFromBytes)) {
//                    System.out.println("Data comes from chat app!");
//
//                    // if someone will send proper broadcast datagram i need to send him info that im alive, info who am i, and my ipaddress, cause he dont know it yet!
//
//                    InetSocketAddress clientAddress = new InetSocketAddress(toRecivePacket.getAddress(), toRecivePacket.getPort());
//                    byte[] request = "Server".getBytes();
//                    DatagramPacket toSendPacket = new DatagramPacket(request, request.length, clientAddress);
//                    datagramSocket.send(toSendPacket);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//        }
    }
}
