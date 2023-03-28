package io.ylab.intensive.lesson04.eventsourcing.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import io.ylab.intensive.lesson04.eventsourcing.ModifyingPersonDao;
import io.ylab.intensive.lesson04.eventsourcing.commands.ModifyingCommand;
import io.ylab.intensive.lesson04.eventsourcing.commands.PersonDelete;
import io.ylab.intensive.lesson04.eventsourcing.commands.PersonSave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static io.ylab.intensive.lesson04.eventsourcing.Constants.PERSON_EXCHANGE_NAME;
import static io.ylab.intensive.lesson04.eventsourcing.Constants.PERSON_QUEUE_NAME;

public class MessageProcessor {
    private static final Logger log = LoggerFactory.getLogger(MessageProcessor.class);

    private static final Map<String, Class<? extends ModifyingCommand>> ROUTING_KEY_TO_COMMAND = Map.of(
            "person.delete", PersonDelete.class,
            "person.save", PersonSave.class
    );

    private final ConnectionFactory connectionFactory;
    private final ModifyingPersonDao personDao;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MessageProcessor(ConnectionFactory connectionFactory, ModifyingPersonDao personDao) {
        this.connectionFactory = connectionFactory;
        this.personDao = personDao;
    }

    public void start() throws IOException, TimeoutException {
        /*
            Получение connection не заключается в блок try-with-resources потому что оно должно оставаться незакрытым,
            так как приложение ожидает входящих сообщений.
         */
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(PERSON_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(PERSON_QUEUE_NAME, false, false, false, null);
        channel.queueBind(PERSON_QUEUE_NAME, PERSON_EXCHANGE_NAME, "person.*");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            log.info("Received message {}", message);
            process(message, delivery.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(PERSON_QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }

    private void process(String message, String routingKey) {
        try {
            Class<? extends ModifyingCommand> commandClass = ROUTING_KEY_TO_COMMAND.get(routingKey);
            if (commandClass == null) {
                log.error("Некорректное значение routingKey {} ", routingKey);
                return;
            }
            ModifyingCommand command = objectMapper.readValue(message, commandClass);
            if (command.isValid()) {
                command.execute(this.personDao);
            } else {
                log.error("Некорректная команда: {}", message);
            }
        } catch (JsonProcessingException | SQLException ex) {
            log.error("Ошибка при обработке сообщения " + message, ex);
        }
    }
}
