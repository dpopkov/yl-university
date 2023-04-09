package io.ylab.intensive.lesson05.eventsourcing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.shared.commands.PersonDelete;
import io.ylab.intensive.lesson05.eventsourcing.shared.commands.PersonSave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static io.ylab.intensive.lesson05.eventsourcing.shared.Constants.*;

@Component
public class RabbitClient {
    private static final Logger log = LoggerFactory.getLogger(RabbitClient.class);

    private final ConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RabbitClient(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void sendDeleteMessage(Long personId) {
        try {
            String message = objectMapper.writeValueAsString(new PersonDelete(personId));
            sendMessage(message, "person.delete");
        } catch (IOException | TimeoutException ex) {
            log.error("Ошибка при попытке удаления Person по id " + personId, ex);
        }
    }

    public void sendSaveMessage(Person person) {
        try {
            String message = objectMapper.writeValueAsString(new PersonSave(person));
            sendMessage(message, "person.save");
        } catch (IOException | TimeoutException ex) {
            log.error("Ошибка при попытке добавления или обновления Person", ex);
        }
    }

    private void sendMessage(String message, String routingKey) throws IOException, TimeoutException {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(PERSON_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(PERSON_QUEUE_NAME, false, false, false, null);
            channel.queueBind(PERSON_QUEUE_NAME, PERSON_EXCHANGE_NAME, "person.*");

            channel.basicPublish(PERSON_EXCHANGE_NAME, routingKey, null, message.getBytes());
            log.info("На routingKey {} отправлено сообщение {}", routingKey, message);
        }
    }
}
