package com.muzzy;

import com.muzzy.domain.Block;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.roles.IncomingNodeRunner;
import com.muzzy.roles.MineRunner;
import com.muzzy.roles.TestThread;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.map.BlockMapService;
import com.muzzy.utils.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
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
    public final static int DIFFICULTY = 2;
    private final URL url = getClass().getResource("/blockchain/block.chain");

    private final ApplicationContext context;
    private final BlockMapService blockMapService;
    private final TransactionOutputService transactionOutputService;

    public Main(ApplicationContext context, BlockMapService blockMapService, TransactionOutputService transactionOutputService) {
        this.context = context;
        this.blockMapService = blockMapService;
        this.transactionOutputService = transactionOutputService;
    }


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
                /**
                 * Save Blockchain
                 */
                Serializer<LinkedHashSet<Block>> blockSerializer = new Serializer<LinkedHashSet<Block>>();
                blockSerializer.serialize(blockMapService.getAll(), url);
                /**
                 * Save UTXO
                 */
                Serializer<Set<TransactionOutput>> utoxSerializer = new Serializer<>();
                utoxSerializer.serialize(transactionOutputService.getAll(), getClass().getResource("/blockchain/utxo.chain") );
            }
        } while (!s.toLowerCase().equals("stop"));
        Main.isStart = false;
        LOG.warn("BYE BYE!");
    }
}
