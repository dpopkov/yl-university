package io.ylab.intensive.lesson04.eventsourcing.db;

import io.ylab.intensive.lesson04.eventsourcing.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModifyingPersonDao {
    private static final Logger log = LoggerFactory.getLogger(ModifyingPersonDao.class);

    private static final String DELETE_SQL = "DELETE FROM person WHERE person_id = ?";
    private static final String SELECT_SQL = "SELECT person_id FROM person WHERE person_id = ?";
    private static final String UPDATE_SQL = "UPDATE person SET first_name = ?, last_name = ?, middle_name = ? WHERE person_id = ?";
    private static final String INSERT_SQL = "INSERT INTO person (person_id, first_name, last_name, middle_name) VALUES (?, ?, ?, ?)";

    private final DataSource dataSource;

    public ModifyingPersonDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deletePerson(Long personId) throws SQLException {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_SQL)) {
            deleteStatement.setLong(1, personId);
            int count = deleteStatement.executeUpdate();
            if (count == 1) {
                log.info("Person удален по id {}", personId);
            } else {
                log.info("Person c id {} не найден", personId);
            }
        }
    }

    public void savePerson(Person person) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            boolean personExists = false;
            try (PreparedStatement selectStatement = connection.prepareStatement(SELECT_SQL)) {
                selectStatement.setLong(1, person.getId());
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    personExists = true;
                }
            }
            if (personExists) {
                try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_SQL)) {
                    updateStatement.setString(1, person.getName());
                    updateStatement.setString(2, person.getLastName());
                    updateStatement.setString(3, person.getMiddleName());
                    updateStatement.setLong(4, person.getId());
                    int count = updateStatement.executeUpdate();
                    log.info("По id {} обновлен {} Person", person.getId(), count);
                }
            } else {
                try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL)) {
                    insertStatement.setLong(1, person.getId());
                    insertStatement.setString(2, person.getName());
                    insertStatement.setString(3, person.getLastName());
                    insertStatement.setString(4, person.getMiddleName());
                    int count = insertStatement.executeUpdate();
                    log.info("Добавлен {} Person", count);
                }
            }
        }
    }
}
