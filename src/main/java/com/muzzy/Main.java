package com.muzzy;

import com.muzzy.cipher.CipherTest;
import com.muzzy.configuration.ConfigLoader;
import com.muzzy.domain.Client;
import com.muzzy.domain.Transaction;

import com.muzzy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@SpringBootApplication
public class Main implements CommandLineRunner {
    @Autowired
    private ConfigLoader configLoader;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ApplicationContext applicationContext;

    private TaskExecutor taskExecutor;
    private ServerSocket serverSocket;
    private Socket socket;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("\nNode Started");
        serverSocket = new ServerSocket(configLoader.getPort());
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Node node = applicationContext.getBean(Node.class).setSocket(socket);
//            taskExecutor.execute(node);
        }

    }


    public static Set<byte[]> getLongStream() {
        Set<Long> doubles = new HashSet<Long>();
        Set<byte[]> byteSet = new HashSet<>();
        new Random().longs(10).forEach(e1 -> {
            doubles.add(e1);
        });

        doubles.forEach(e -> byteSet.add(e.toString().getBytes()));
        return byteSet;
    }

    public static void getEncode() {
        CipherTest cipherTest = new CipherTest();
        Set<byte[]> bytes = getLongStream();
        Set<byte[]> encryptedBytes = new HashSet<>();
        bytes.forEach(x -> encryptedBytes.add(cipherTest.Encode(x)));

        bytes.forEach(x -> System.out.println(Arrays.toString(x)));
        encryptedBytes.forEach(x -> System.out.println(Arrays.toString(x)));
        System.out.println("Confirm!");
    }

    public static void luckyNumber() {
        Random random = new Random();
        int border = 1000000;
        int luckyNumber = random.nextInt(border);
        long l = 0;
        do {
            l++;
//            System.out.println(l);
            if (l % 10000 == 0) {
                System.out.print(".");
            }

        } while (luckyNumber != random.nextInt(border));
        System.out.println("Lucky number is: " + luckyNumber);
    }


}

//        final InetSocketAddress inetSocketAddress = new InetSocketAddress("0.0.0.0", configLoader.getPort());
//        ServerSocket serverSocket = new ServerSocket(configLoader.getPort());
//        Socket socket = null;
//
//        //run server connector
//        new Thread(new TestThread()).start();
//
//        //run server listener
//
//        while (true) {
//            socket = serverSocket.accept();
//            new Thread(new Node(socket)).start();
//        }


//        for (int i = 0; i < 100; i++) {
//            long startTime = System.nanoTime();
//            LocalTime start = LocalTime.now();
//            getLongStream();
//            luckyNumber();
//            getEncode();
//            System.out.println(System.nanoTime() - startTime);
//            LocalTime stop = LocalTime.now();
//            System.out.println(Duration.between(start, stop).toString());
//            System.out.println(Arrays.toString(configLoader.getAddresses().toArray()));
//        }