package io.ylab.intensive.lesson05.eventsourcing.db;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DbApp {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.registerShutdownHook();
        applicationContext.start();

        MessageProcessor processor = applicationContext.getBean(MessageProcessor.class);
        processor.start();
    }
}
