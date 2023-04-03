package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Простейшее demo (не Spring) для тестирования отправки и получения обработанных сообщений.
 * Для работы необходимо чтобы было уже запущено приложение MessageFilterApp.
 */
public class SimpleTestingCliClient {
    private static final Logger log = LoggerFactory.getLogger(SimpleTestingCliClient.class);

    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in);
             RabbitClient rabbitClient = new RabbitClient(new Config().connectionFactory())) {
            rabbitClient.startReceiving();
            boolean isRunning = true;
            while (isRunning) {
                System.out.print("Введите сообщение для отправки: ");
                String text = scanner.nextLine();
                if ("exit".equalsIgnoreCase(text)) {
                    isRunning = false;
                } else {
                    rabbitClient.send(text);
                    giveTimeForProcessing();
                }
            }
        }
    }

    private static class RabbitClient implements AutoCloseable {
        private final ConnectionFactory connectionFactory;
        private Channel channel;
        private Connection connection;

        private RabbitClient(ConnectionFactory connectionFactory) {
            this.connectionFactory = connectionFactory;
        }

        public void startReceiving() throws IOException, TimeoutException {
            this.connection = connectionFactory.newConnection();
            this.channel = connection.createChannel();
            this.channel.queueDeclare(Constants.INPUT_QUEUE, false, false, false, null);
            this.channel.queueDeclare(Constants.OUTPUT_QUEUE, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                log.info("Получено сообщение: {}", message);
            };
            this.channel.basicConsume(Constants.OUTPUT_QUEUE, true, deliverCallback, consumerTag -> {
            });
        }

        public void send(String text) throws IOException {
            this.channel.basicPublish("", Constants.INPUT_QUEUE, null, text.getBytes());
        }

        @Override
        public void close() throws Exception {
            this.channel.close();
            this.connection.close();
        }
    }

    private static void giveTimeForProcessing() {
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
