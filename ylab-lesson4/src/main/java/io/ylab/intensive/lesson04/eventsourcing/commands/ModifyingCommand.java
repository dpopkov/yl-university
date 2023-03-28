package io.ylab.intensive.lesson04.eventsourcing.commands;

import io.ylab.intensive.lesson04.eventsourcing.ModifyingPersonDao;

import java.sql.SQLException;

public interface ModifyingCommand {

    boolean isValid();

    void execute(ModifyingPersonDao personDao) throws SQLException;
}
