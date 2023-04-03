package io.ylab.intensive.lesson05.eventsourcing.db;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.shared.ModifyingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class DbClient implements ModifyingDao<Person> {
    private static final Logger log = LoggerFactory.getLogger(DbClient.class);

    private static final String DELETE_SQL = "DELETE FROM person WHERE person_id = ?";
    private static final String SELECT_SQL = "SELECT person_id FROM person WHERE person_id = ?";
    private static final String UPDATE_SQL = "UPDATE person SET first_name = ?, last_name = ?, middle_name = ? WHERE person_id = ?";
    private static final String INSERT_SQL = "INSERT INTO person (person_id, first_name, last_name, middle_name) VALUES (?, ?, ?, ?)";

    private final DataSource dataSource;

    public DbClient(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void delete(Long personId) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(DELETE_SQL)) {
            setNullableLongId(deleteStatement, 1, personId);
            int count = deleteStatement.executeUpdate();
            if (count == 1) {
                log.info("Person удален по id {}", personId);
            } else {
                log.info("Person c id {} не найден", personId);
            }
        } catch (SQLException sqlEx) {
            log.error("Ошибка при удалении Person по id " + personId, sqlEx);
        }
    }

    @Override
    public void save(Person person) {
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
        } catch (SQLException sqlEx) {
            log.error("Ошибка при сохранении Person", sqlEx);
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
