package io.ylab.intensive.lesson04.eventsourcing.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ylab.intensive.lesson04.eventsourcing.Person;

public class PersonSave {
    private final Command command = Command.SAVE;
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

    public Command getCommand() {
        return command;
    }

    @JsonIgnore
    public boolean isValid() {
        return this.command == Command.SAVE && person != null && person.getId() != null;
    }
}
