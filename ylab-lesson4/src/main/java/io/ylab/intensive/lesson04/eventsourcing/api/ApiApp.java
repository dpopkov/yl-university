package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;
import java.util.List;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = initMQ();
        DataSource dataSource = DbUtil.buildDataSource();

        ReadingPersonDao personDao = new ReadingPersonDao(dataSource);
        MessageSender messageSender = new MessageSender(connectionFactory);
        PersonApi personApi = new PersonApiImpl(messageSender, personDao);

        String mode = args.length > 0 ? args[0] : "interactive";
        if ("interactive".equalsIgnoreCase(mode)) {
            ConsoleCLI cli = new ConsoleCLI(personApi, System.in, System.out);
            cli.start();
        } else {
            personApi.savePerson(1L, "John", "Doe", "J");
            personApi.savePerson(2L, "Jane", "Doe", "");
            printPersons(personApi.findAll());

            Person john = personApi.findPerson(1L);
            System.out.println("Found: " + john);

            personApi.deletePerson(2L);
            printPersons(personApi.findAll());

            personApi.savePerson(1L, "John", "Modified", "");
            printPersons(personApi.findAll());
        }
    }

    private static void printPersons(List<Person> list) {
        System.out.println("Found " + list.size() + " Persons");
        for (Person person : list) {
            System.out.println(person);
        }
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }
}
