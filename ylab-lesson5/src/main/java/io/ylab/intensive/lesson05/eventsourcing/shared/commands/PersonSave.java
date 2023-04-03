package io.ylab.intensive.lesson05.eventsourcing.shared.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.shared.ModifyingDao;

public class PersonSave implements ModifyingCommand {
    private final CommandType command = CommandType.SAVE;
    private Person person;

    public PersonSave() {
    }

    public PersonSave(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public CommandType getCommand() {
        return command;
    }

    @JsonIgnore
    @Override
    public boolean isValid() {
        return this.command == CommandType.SAVE && person != null && person.getId() != null;
    }

    @Override
    public void execute(ModifyingDao<Person> personDao) {
        personDao.save(this.person);
    }
}
