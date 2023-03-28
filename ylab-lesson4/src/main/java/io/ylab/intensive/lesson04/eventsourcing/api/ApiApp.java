package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        Optional<DataSource> optional = getDataSourceWithExistingTable();
        if (optional.isEmpty()) {
            System.out.println("Запустите первым приложение DbApp и попробуйте еще раз.");
            return;
        }
        DataSource dataSource = optional.get();
        ConnectionFactory connectionFactory = initMQ();

        DbReadingPersonDao personDao = new DbReadingPersonDao(dataSource);
        MessageSender messageSender = new MessageSender(connectionFactory);
        PersonApi personApi = new PersonApiImpl(messageSender, personDao);

        String mode = args.length > 0 ? args[0] : "interactive";
        if ("interactive".equalsIgnoreCase(mode)) {
            ConsoleCLI cli = new ConsoleCLI(personApi, System.in, System.out);
            cli.start();
        } else {
            personApi.savePerson(1L, "John", "Doe", "J");
            personApi.savePerson(2L, "Jane", "Doe", "");
            giveTimeToExecute();
            printPersons(personApi.findAll());

            Person john = personApi.findPerson(1L);
            System.out.println("Found: " + john);

            personApi.deletePerson(2L);
            giveTimeToExecute();
            printPersons(personApi.findAll());

            personApi.savePerson(1L, "John", "Modified", "");
            giveTimeToExecute();
            printPersons(personApi.findAll());
        }
    }

    private static void giveTimeToExecute() {
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printPersons(List<Person> list) {
        System.out.println("Found " + list.size() + " Persons");
        for (Person person : list) {
            System.out.println(person);
        }
    }

    private static Optional<DataSource> getDataSourceWithExistingTable() {
        try {
            DataSource dataSource = DbUtil.buildDataSource();
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeQuery("SELECT EXISTS (SELECT first_name FROM person)");
            }
            return Optional.of(dataSource);
        } catch (SQLException sqlException) {
            String message = sqlException.getMessage();
            if (message.contains("person") && message.contains("does not exist")) {
                System.err.println("Таблица 'person' еще не создана.");
            } else {
                System.err.println(message);
            }
            return Optional.empty();
        }
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }
}
