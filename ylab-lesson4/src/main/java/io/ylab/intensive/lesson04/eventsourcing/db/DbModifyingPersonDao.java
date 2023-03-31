package io.ylab.intensive.lesson04.eventsourcing.db;

import io.ylab.intensive.lesson04.eventsourcing.ModifyingPersonDao;
import io.ylab.intensive.lesson04.eventsourcing.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

public class DbModifyingPersonDao implements ModifyingPersonDao {
    private static final Logger log = LoggerFactory.getLogger(DbModifyingPersonDao.class);

    private static final String DELETE_SQL = "DELETE FROM person WHERE person_id = ?";
    private static final String SELECT_SQL = "SELECT person_id FROM person WHERE person_id = ?";
    private static final String UPDATE_SQL = "UPDATE person SET first_name = ?, last_name = ?, middle_name = ? WHERE person_id = ?";
    private static final String INSERT_SQL = "INSERT INTO person (person_id, first_name, last_name, middle_name) VALUES (?, ?, ?, ?)";

    private final DataSource dataSource;

    public DbModifyingPersonDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void deletePerson(Long personId) throws SQLException {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_SQL)) {
            setNullableLongId(deleteStatement, 1, personId);
            int count = deleteStatement.executeUpdate();
            if (count == 1) {
                log.info("Person удален по id {}", personId);
            } else {
                log.info("Person c id {} не найден", personId);
            }
        }
    }

    @Override
    public void savePerson(Person person) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            boolean personExists = false;
            try (PreparedStatement selectStatement = connection.prepareStatement(SELECT_SQL)) {
                setNullableLongId(selectStatement, 1, person.getId());
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    personExists = true;
                }
            }
            if (personExists) {
                try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_SQL)) {
                    setNullableString(updateStatement, 1, person.getName());
                    setNullableString(updateStatement, 2, person.getLastName());
                    setNullableString(updateStatement, 3, person.getMiddleName());
                    setNullableLongId(updateStatement, 4, person.getId());
                    int count = updateStatement.executeUpdate();
                    log.info("По id {} обновлен {} Person", person.getId(), count);
                }
            } else {
                try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL)) {
                    setNullableLongId(insertStatement, 1, person.getId());
                    setNullableString(insertStatement, 2, person.getName());
                    setNullableString(insertStatement, 3, person.getLastName());
                    setNullableString(insertStatement, 4, person.getMiddleName());
                    int count = insertStatement.executeUpdate();
                    log.info("Добавлен {} Person", count);
                }
            }
        }
    }

    private void setNullableString(PreparedStatement statement, int parameterIndex, String value) throws SQLException {
        if (value != null) {
            statement.setString(parameterIndex, value);
        } else {
            statement.setNull(parameterIndex, Types.VARCHAR);
        }
    }

    private void setNullableLongId(PreparedStatement statement, int parameterIndex, Long id) throws SQLException {
        if (id == null) {
            throw new NullPointerException("Значение ID не должно быть null");
        }
        statement.setLong(parameterIndex, id);
    }
}
