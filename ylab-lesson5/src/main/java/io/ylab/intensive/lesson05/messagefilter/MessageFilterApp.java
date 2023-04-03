package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * После первого запуска с настройками по умолчанию в корне проекта должен появиться файл vulgarism.txt
 * из которого далее будет происходить загрузка слов для фильтрации.
 */
public class MessageFilterApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.registerShutdownHook();
        applicationContext.start();

        MessageProcessor processor = applicationContext.getBean(MessageProcessor.class);
        processor.start();
    }
}
