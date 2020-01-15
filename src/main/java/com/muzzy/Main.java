package com.muzzy;

import com.muzzy.cipher.CipherTest;
import com.muzzy.cipher.Cipherable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

//@SpringBootApplication
public class Main {
    public static void main(String[] args) {
//        SpringApplication.run(Main.class, args);

        for (int i = 0; i < 100; i++) {
            long startTime = System.nanoTime();
            LocalTime start = LocalTime.now();
//            getLongStream();
//            luckyNumber();
            getEncode();
            System.out.println(System.nanoTime() - startTime);
            LocalTime stop = LocalTime.now();
            System.out.println(Duration.between(start, stop).toString());

        }
    }

    public static Set<byte[]> getLongStream() {
        Set<Long> doubles = new HashSet<Long>();
        Set<byte[]> byteSet = new HashSet<>();
        new Random().longs(10).forEach(e1 -> {
//            System.out.println(e1);
            doubles.add(e1);
        });

        doubles.forEach(e -> byteSet.add(e.toString().getBytes()));
        return byteSet;
    }

    public static void getEncode() {
        Cipherable cipherTest = new CipherTest();
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
