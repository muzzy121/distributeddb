package com.muzzy;

import com.muzzy.configuration.ConfigLoader;
import com.muzzy.roles.Node;
import com.muzzy.roles.TestThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

@Component
//@PropertySource("application.yml")
public class AppMain implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ApplicationContext context;

//    @Qualifier("applicationTaskExecutor")
    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ConfigLoader configLoader;

    private ServerSocket serverSocket;
    private Socket socket;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {

//            System.out.println("\nTestTask Started");
//            taskExecutor.execute(context.getBean(TestThread.class));
//            context.getBean(Main.class).getEncode();

        }
    }
}
