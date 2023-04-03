package io.ylab.intensive.lesson05.eventsourcing.shared.commands;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.shared.ModifyingDao;

public interface ModifyingCommand {

    boolean isValid();

    void execute(ModifyingDao<Person> personDao);
}
