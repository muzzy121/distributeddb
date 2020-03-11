package com.muzzy;

import com.muzzy.domain.Block;
import com.muzzy.roles.IncomingNodeRunner;
import com.muzzy.roles.MineRunner;
import com.muzzy.roles.TestThread;
import com.muzzy.service.map.BlockMapService;
import com.muzzy.utils.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@SpringBootApplication
public class Main implements CommandLineRunner {
    public static boolean isStart = false;
    public static UUID nodeId;
    private final Logger LOG = LoggerFactory.getLogger(Main.class);
    private Scanner scanner = new Scanner(System.in);
    private ExecutorService executorService;
    private final URL url = getClass().getResource("/blockchain/block.chain");

    @Autowired
    private ApplicationContext context;

    @Autowired
    private BlockMapService blockMapService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String s = "";
        do {
            if (s.equals("") || s.isEmpty()) {
                System.out.print("Miner ready (write command): ");
            }
            s = scanner.nextLine();
            if (s.toLowerCase().equals("start")) {
                Main.isStart = true;
                executorService = Executors.newSingleThreadExecutor();
                Future future = executorService.submit(context.getBean(MineRunner.class));
                executorService.shutdown();
//            context.getBean(MineRunner.class).mining(); // TODO: 2020-02-05 Zjebałem tu
            }
            if (s.toLowerCase().equals("listen")) {
//            Main.isStart=true;
                executorService = Executors.newSingleThreadExecutor();
                Future future = executorService.submit(context.getBean(IncomingNodeRunner.class));
                executorService.shutdown();
            }
            if (s.toLowerCase().equals("test")) {
//            Main.isStart=true;
                executorService = Executors.newSingleThreadExecutor();
                Future future = executorService.submit(context.getBean(TestThread.class));
                executorService.shutdown();
            }
            if (s.toLowerCase().equals("save")) {
//            Main.isStart=true;
                Serializer<LinkedHashSet<Block>> serializer = new Serializer<LinkedHashSet<Block>>();
                serializer.serialize(blockMapService.getAll(), url);
            }
        } while (!s.toLowerCase().equals("stop"));
        Main.isStart = false;
        LOG.warn("BYE BYE!");

        // Set server socket and start new Thread after
    }
}
