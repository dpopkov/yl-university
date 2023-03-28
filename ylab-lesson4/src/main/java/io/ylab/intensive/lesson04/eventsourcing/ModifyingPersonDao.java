package io.ylab.intensive.lesson04.eventsourcing;

import java.sql.SQLException;

public interface ModifyingPersonDao {

    void deletePerson(Long personId) throws SQLException;

    void savePerson(Person person) throws SQLException;
}
