package io.ylab.intensive.lesson04.eventsourcing.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PersonDelete {
    private final Command command = Command.DELETE;
    private Long personId;

    public PersonDelete() {
    }

    public PersonDelete(Long personId) {
        this.personId = personId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Command getCommand() {
        return command;
    }

    @JsonIgnore
    public boolean isValid() {
        return this.command == Command.DELETE && personId != null;
    }
}
