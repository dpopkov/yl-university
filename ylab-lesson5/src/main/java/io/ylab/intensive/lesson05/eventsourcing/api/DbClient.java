package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.shared.ReadingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DbClient implements ReadingDao<Person> {
    private static final Logger log = LoggerFactory.getLogger(DbClient.class);

    private static final String SELECT_BY_ID_SQL = "SELECT first_name, last_name, middle_name FROM person WHERE person_id = ?";
    private static final String SELECT_ALL_SQL = "SELECT person_id, first_name, last_name, middle_name FROM person";

    private final DataSource dataSource;

    public DbClient(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Person> findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            selectStatement.setLong(1, id);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString(1);
                String lastName = resultSet.getString(2);
                String middleName = resultSet.getString(3);
                Person person = new Person(id, firstName, lastName, middleName);
                return Optional.of(person);
            } else {
                return Optional.empty();
            }
        } catch (SQLException sqlEx) {
            processSqlException(sqlEx, "Ошибка при попытке найти Person по id " + id);
            return Optional.empty();
        }
    }

    @Override
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
            processSqlException(sqlEx, "Ошибка при получении всех Person");
            return persons;
        }
    }

    private void processSqlException(SQLException sqlException, String errorMessage) {
        if (tablePersonIsNotExisting(sqlException)) {
            System.out.println("Данные отсутствуют. Не выходя из приложения ApiApp запустите параллельно DbApp.");
            System.out.println("После чего выполните добавление Person и повторите попытку получения данных.");
        } else {
            log.error(errorMessage, sqlException);
        }
    }

    private boolean tablePersonIsNotExisting(SQLException sqlException) {
        String message = sqlException.getMessage();
        return message.contains("person") && message.contains("does not exist");
    }
}
