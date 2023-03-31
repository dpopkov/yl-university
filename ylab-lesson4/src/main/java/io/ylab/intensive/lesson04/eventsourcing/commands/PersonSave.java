package io.ylab.intensive.lesson04.eventsourcing.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ylab.intensive.lesson04.eventsourcing.ModifyingPersonDao;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import java.sql.SQLException;

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
    public void execute(ModifyingPersonDao personDao) throws SQLException {
        personDao.savePerson(this.person);
    }
}
