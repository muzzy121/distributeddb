package com.muzzy;

import com.muzzy.bootstrap.Bootstrap;
import com.muzzy.configuration.ConfigLoader;
import com.muzzy.roles.Miner;
import com.muzzy.roles.TestThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
//@PropertySource("application.yml")
public class MineRunner implements ApplicationListener<ContextRefreshedEvent> {

    public static boolean notMined = true;
    private ApplicationContext context;
    private ConfigLoader configLoader;
    private ExecutorService executorService;

    public MineRunner(ApplicationContext context, ConfigLoader configLoader) {
        this.context = context;
        this.configLoader = configLoader;
    }

    private ServerSocket serverSocket;
    private Socket socket;

    public void mining() {
        long i=0;
        notMined = false;
        executorService = Executors.newFixedThreadPool(2);
        Future future = executorService.submit(context.getBean(Miner.class));
        executorService.shutdown();

        while (Main.isStart) {
            if(i++ % 1000000000 == 0) {
                System.out.print(".");
            }
            if (notMined == true) {
                System.out.print("+");
                mining();
            }
        }
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {


            //System.out.println("\nTestTask Started");
//            taskExecutor.execute(context.getBean(TestThread.class));
//            context.getBean(Main.class).getEncode();

        }
    }
}
