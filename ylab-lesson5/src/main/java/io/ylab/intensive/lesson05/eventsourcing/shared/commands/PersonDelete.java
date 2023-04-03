package io.ylab.intensive.lesson05.eventsourcing.shared.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.shared.ModifyingDao;

public class PersonDelete implements ModifyingCommand {
    private final CommandType commandType = CommandType.DELETE;
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

    public CommandType getCommandType() {
        return commandType;
    }

    @JsonIgnore
    @Override
    public boolean isValid() {
        return this.commandType == CommandType.DELETE && personId != null;
    }

    @Override
    public void execute(ModifyingDao<Person> personDao) {
        personDao.delete(this.personId);
    }
}
