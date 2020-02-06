package com.muzzy.roles;

import com.muzzy.Main;
import com.muzzy.configuration.ConfigLoader;
import com.muzzy.service.TransactionService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
//@PropertySource("application.yml")
public class MineRunner implements Callable<Integer> {
    public static boolean notMined = true;
    private final ApplicationContext context;
    private final ConfigLoader configLoader;
    private final TransactionService transactionService;
    private final OutgoingNode outgoingNode;
    private ExecutorService executorService;

    public MineRunner(ApplicationContext context, ConfigLoader configLoader, TransactionService transactionService, OutgoingNode outgoingNode) {
        this.context = context;
        this.configLoader = configLoader;
        this.transactionService = transactionService;
        this.outgoingNode = outgoingNode;
    }
    @Override
    public Integer call() throws Exception {
        mining();
        return null;
    }

    public void mining() {
        long i = 0;
        notMined = false;
        if (!transactionService.getAll().isEmpty()) {
            outgoingNode.run();
        }
        executorService = Executors.newFixedThreadPool(2);
        Future future = executorService.submit(context.getBean(Miner.class));
        executorService.shutdown();

        while (Main.isStart) {
            if (i++ % 1000000000 == 0) {
                System.out.print(".");
            }
            if (notMined == true) {
                System.out.print("+");
                mining();
            }
        }
    }
}
//    public void sendTransaction(){
////        transactions.addAll(transactionService.getAll());
//
////        Set<Socket> socketSet = connector.connect();
////        Set<Socket> socketSet = testThread.getConnectedSockets();
//        System.out.println("Before list");
//        socketSet.forEach(socket -> {
//            try {
//                System.out.println("After list");
////                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                objectOutputStream.writeObject(transactions);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
//    }


    //    @Override
//    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//
//        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
//
//
//            System.out.println("\nTestTask Started");
//            taskExecutor.execute(context.getBean(TestThread.class));
//            context.getBean(Main.class).getEncode();
//
//        }
//    }
