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

@Component
//@PropertySource("application.yml")
public class AppMain implements ApplicationListener<ContextRefreshedEvent> {
    private Scanner scanner = new Scanner(System.in);

    private ApplicationContext context;
    private ThreadPoolTaskExecutor taskExecutor;
    private ConfigLoader configLoader;


    public AppMain(ApplicationContext context, ThreadPoolTaskExecutor taskExecutor, ConfigLoader configLoader) {
        this.context = context;
        this.taskExecutor = taskExecutor;
        this.configLoader = configLoader;
    }

    private ServerSocket serverSocket;
    private Socket socket;

    public void mining() {
//        taskExecutor.setCorePoolSize(0);
        taskExecutor.setCorePoolSize(8);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setQueueCapacity(25);
        Main.notMined = true;
        taskExecutor.execute(context.getBean(Miner.class));
//        for (int cpu = 0; cpu < Runtime.getRuntime().availableProcessors(); cpu++) {
        for (int cpu = 0; cpu < 8; cpu++) {
//            taskExecutor.execute(context.getBean(Miner.class));
        }
//        Map<String, Miner> map = context.getBeansOfType(Miner.class);

        while (Main.isStart) {
            if (Main.notMined == false) {
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
