package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class MessageProcessor {
    private static final Logger log = LoggerFactory.getLogger(MessageProcessor.class);

    private final ConnectionFactory connectionFactory;
    private final MessageFilter messageFilter;
    private Channel channel;
    private Connection connection;

    public MessageProcessor(ConnectionFactory connectionFactory, MessageFilter messageFilter) {
        this.connectionFactory = connectionFactory;
        this.messageFilter = messageFilter;
    }

    public void start() {
        try {
            this.connection = connectionFactory.newConnection();
            this.channel = connection.createChannel();
            this.channel.queueDeclare(Constants.INPUT_QUEUE, false, false, false, null);
            this.channel.queueDeclare(Constants.OUTPUT_QUEUE, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                log.info("Получено сообщение: {}", message);
                process(message);
            };
            this.channel.basicConsume(Constants.INPUT_QUEUE, true, deliverCallback, consumerTag -> {
            });
            log.info("MessageProcessor готов к обработке сообщений.");
        } catch (TimeoutException | IOException e) {
            log.error("Ошибка при обработке сообщений.", e);
        }
    }

    private void process(String message) throws IOException {
        String filtered = this.messageFilter.filter(message);
        this.channel.basicPublish("", Constants.OUTPUT_QUEUE, null, filtered.getBytes());
    }

    @PreDestroy
    public void stop() {
        try {
            log.info("Завершение работы процессора сообщений.");
            this.channel.close();
            this.connection.close();
        } catch (IOException | TimeoutException ioException) {
            log.error("Ошибка при завершении работы.", ioException);
        }
    }
}
