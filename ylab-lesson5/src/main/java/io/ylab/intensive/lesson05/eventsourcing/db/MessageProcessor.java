package io.ylab.intensive.lesson05.eventsourcing.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import io.ylab.intensive.lesson05.eventsourcing.shared.commands.ModifyingCommand;
import io.ylab.intensive.lesson05.eventsourcing.shared.commands.PersonDelete;
import io.ylab.intensive.lesson05.eventsourcing.shared.commands.PersonSave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static io.ylab.intensive.lesson05.eventsourcing.shared.Constants.PERSON_EXCHANGE_NAME;
import static io.ylab.intensive.lesson05.eventsourcing.shared.Constants.PERSON_QUEUE_NAME;

@Component
public class MessageProcessor {
    private static final Logger log = LoggerFactory.getLogger(MessageProcessor.class);

    private final Map<String, Class<? extends ModifyingCommand>> routingKeyToCommandMap = new HashMap<>();
    private final ConnectionFactory connectionFactory;
    private final DbClient dbClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Channel channel;
    private Connection connection;

    public MessageProcessor(ConnectionFactory connectionFactory, DbClient dbClient) {
        this.connectionFactory = connectionFactory;
        this.dbClient = dbClient;
        addCommand("person.delete", PersonDelete.class);
        addCommand("person.save", PersonSave.class);
    }

    public void addCommand(String routingKey, Class<? extends ModifyingCommand> commandClass) {
        this.routingKeyToCommandMap.put(routingKey, commandClass);
    }

    public void start() throws IOException, TimeoutException {
        this.connection = connectionFactory.newConnection();
        this.channel = connection.createChannel();

        this.channel.exchangeDeclare(PERSON_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        this.channel.queueDeclare(PERSON_QUEUE_NAME, false, false, false, null);
        this.channel.queueBind(PERSON_QUEUE_NAME, PERSON_EXCHANGE_NAME, "person.*");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            log.info("Received message {}", message);
            process(message, delivery.getEnvelope().getRoutingKey());
        };
        this.channel.basicConsume(PERSON_QUEUE_NAME, true, deliverCallback, consumerTag -> {});
        log.info("MessageProcessor is ready to process messages.");
    }

    @PreDestroy
    public void stop() {
        try {
            log.info("Closing connection");
            this.channel.close();
            this.connection.close();
        } catch (IOException | TimeoutException ioException) {
            log.error("Ошибка при завершении работы", ioException);
        }
    }

    private void process(String message, String routingKey) {
        try {
            Class<? extends ModifyingCommand> commandClass = this.routingKeyToCommandMap.get(routingKey);
            if (commandClass == null) {
                log.error("Неизвестное значение routingKey {} ", routingKey);
                return;
            }
            ModifyingCommand command = this.objectMapper.readValue(message, commandClass);
            if (command.isValid()) {
                command.execute(this.dbClient);
            } else {
                log.error("Некорректная команда: {}", message);
            }
        } catch (JsonProcessingException ex) {
            log.error("Ошибка при обработке сообщения " + message, ex);
        }
    }
}
