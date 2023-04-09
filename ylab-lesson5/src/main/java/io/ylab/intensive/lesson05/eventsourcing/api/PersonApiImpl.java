package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonApiImpl implements PersonApi {

    private final RabbitClient rabbitClient;
    private final DbClient dbClient;

    public PersonApiImpl(RabbitClient rabbitClient, DbClient dbClient) {
        this.rabbitClient = rabbitClient;
        this.dbClient = dbClient;
    }

    @Override
    public void deletePerson(Long personId) {
        this.rabbitClient.sendDeleteMessage(personId);
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        this.rabbitClient.sendSaveMessage(new Person(personId, firstName, lastName, middleName));
    }

    @Override
    public Person findPerson(Long personId) {
        return this.dbClient.findById(personId).orElse(null);
    }

    @Override
    public List<Person> findAll() {
        return this.dbClient.findAll();
    }
}
