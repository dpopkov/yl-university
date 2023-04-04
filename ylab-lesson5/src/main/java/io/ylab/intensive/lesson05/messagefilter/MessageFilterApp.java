package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Приложение берет список слов для фильтрации из файла в корне проекта.
 * Имя и формат файла задаются в файле src/main/resources/messagefilter.properties
 * Список слов изначально хранится в бинарном файле.
 * После первого запуска с настройками по умолчанию в корне проекта должен появиться текстовый файл vulgarism.txt
 * из которого далее (и при последующих запусках) будет происходить загрузка слов для фильтрации.
 * Для использования другого списка слов необходимо либо изменить настройки в messagefilter.properties,
 * либо скопировать новый список слов в файл vulgarism.txt.
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
