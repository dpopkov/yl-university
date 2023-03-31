package io.ylab.intensive.lesson04.eventsourcing.api;

import io.ylab.intensive.lesson04.eventsourcing.Person;

import java.util.List;

/**
 * Реализацию PersonApi в которой для выполнения команд модифицирующих данные
 * используется экземпляр MessageSender, отправляющий сообщения через очередь,
 * а для выполнения команд чтения используется ReadingPersonDao, который обращается
 * напрямую к базе данных.
 */
public class PersonApiImpl implements PersonApi {

    private final MessageSender messageSender;
    private final DbReadingPersonDao personDao;

    public PersonApiImpl(MessageSender messageSender, DbReadingPersonDao personDao) {
        this.messageSender = messageSender;
        this.personDao = personDao;
    }

    @Override
    public void deletePerson(Long personId) {
        messageSender.sendDeleteMessage(personId);
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        messageSender.sendSaveMessage(new Person(personId, firstName, lastName, middleName));
    }

    @Override
    public Person findPerson(Long personId) {
        return personDao.findById(personId).orElse(null);
    }

    @Override
    public List<Person> findAll() {
        return personDao.findAll();
    }
}
