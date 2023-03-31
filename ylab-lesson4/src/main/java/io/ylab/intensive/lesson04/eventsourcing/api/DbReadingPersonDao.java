package io.ylab.intensive.lesson04.eventsourcing.api;

import io.ylab.intensive.lesson04.eventsourcing.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbReadingPersonDao {
    private static final Logger log = LoggerFactory.getLogger(DbReadingPersonDao.class);

    private static final String SELECT_BY_ID_SQL = "SELECT first_name, last_name, middle_name FROM person WHERE person_id = ?";
    private static final String SELECT_ALL_SQL = "SELECT person_id, first_name, last_name, middle_name FROM person";

    private final DataSource dataSource;

    public DbReadingPersonDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<Person> findById(Long personId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            selectStatement.setLong(1, personId);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString(1);
                String lastName = resultSet.getString(2);
                String middleName = resultSet.getString(3);
                Person person = new Person(personId, firstName, lastName, middleName);
                return Optional.of(person);
            } else {
                return Optional.empty();
            }
        } catch (SQLException sqlEx) {
            log.error("Ошибка при попытке найти Person по id " + personId, sqlEx);
            return Optional.empty();
        }
    }

    public List<Person> findAll() {
        List<Person> persons = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement selectStatement = connection.createStatement()) {
            ResultSet resultSet = selectStatement.executeQuery(SELECT_ALL_SQL);
            while (resultSet.next()) {
                Long personId = resultSet.getLong(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                String middleName = resultSet.getString(4);
                Person person = new Person(personId, firstName, lastName, middleName);
                persons.add(person);
            }
            return persons;
        } catch (SQLException sqlEx) {
            log.error("Ошибка при получении всех Person", sqlEx);
            return persons;
        }
    }
}
