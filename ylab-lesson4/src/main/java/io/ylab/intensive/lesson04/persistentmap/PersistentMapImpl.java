package io.ylab.intensive.lesson04.persistentmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Класс, методы которого надо реализовать
 */
public class PersistentMapImpl implements PersistentMap {

    private static final String SELECT_VALUE_SQL = "SELECT value FROM persistent_map WHERE map_name = ? AND KEY = ? ";
    private static final String SELECT_KEY_SQL = "SELECT KEY FROM persistent_map WHERE map_name = ?";
    private static final String DELETE_SQL = "DELETE FROM persistent_map WHERE map_name = ? AND KEY = ? ";
    private static final String INSERT_SQL = "INSERT INTO persistent_map (map_name, key, value) VALUES (? ,?, ?)";
    private static final String UPDATE_SQL = "UPDATE persistent_map SET value = ? WHERE map_name = ? AND key = ?";
    private static final String CLEAR_SQL = "DELETE FROM persistent_map WHERE map_name = ?";

    private static final Logger log = LoggerFactory.getLogger(PersistentMapImpl.class);

    private final DataSource dataSource;
    private String currentMapName;

    public PersistentMapImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void init(String name) {
        this.currentMapName = name;
    }

    @Override
    public boolean containsKey(String key) throws SQLException {
        checkMapIsInitialized();
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement selectStatement = withMapNameKey(connection, SELECT_VALUE_SQL, key);
             ResultSet resultSet = selectStatement.executeQuery()) {
            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getKeys() throws SQLException {
        checkMapIsInitialized();
        List<String> keys = new ArrayList<>();
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement selectStatement = withMapName(connection, SELECT_KEY_SQL);
             ResultSet resultSet = selectStatement.executeQuery()) {
            while (resultSet.next()) {
                keys.add(resultSet.getString(1));
            }
        }
        log.info("Returning {} keys for map '{}'", keys.size(), this.currentMapName);
        return keys;
    }

    @Override
    public String get(String key) throws SQLException {
        checkMapIsInitialized();
        String value = null;
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement selectStatement = withMapNameKey(connection, SELECT_VALUE_SQL, key);
             ResultSet resultSet = selectStatement.executeQuery()) {
            if (resultSet.next()) {
                value = resultSet.getString(1);
                log.info("Found value '{}' for key '{}' in map '{}'", value, key, this.currentMapName);
            }
        }
        return value;
    }

    @Override
    public void remove(String key) throws SQLException {
        checkMapIsInitialized();
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement deleteStatement = withMapNameKey(connection, DELETE_SQL, key)) {
            int count = deleteStatement.executeUpdate();
            if (count == 1) {
                log.info("Key '{}' removed from map '{}'", key, this.currentMapName);
            } else {
                log.info("Key '{}' not found in the map '{}'", key, this.currentMapName);
            }
        }
    }

    @Override
    public void put(String key, String value) throws SQLException {
        checkMapIsInitialized();
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement selectStatement = withMapNameKey(connection, SELECT_VALUE_SQL, key);
             ResultSet resultSet = selectStatement.executeQuery()) {
            if (resultSet.next()) {
                /*
                В этом месте я первоначально делал проверку if (!Objects.equals(oldValue, value))
                и в случае отличия выполнял update с использованием UPDATE_SQL.
                Но поскольку в задании написано "сначала удаляет существующую пару из Map",
                то делаю удаление.
                 */
                try (PreparedStatement deleteStatement = withMapNameKey(connection, DELETE_SQL, key)) {
                    deleteStatement.executeUpdate();
                }
            }
            try (PreparedStatement insertStatement = withMapNameKeyValue(connection, INSERT_SQL, key, value)) {
                insertStatement.executeUpdate();
                log.info("Key '{}' value '{}' inserted into map '{}'", key, value, this.currentMapName);
            }
        }
    }

    @Override
    public void clear() throws SQLException {
        checkMapIsInitialized();
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement clearStatement = withMapName(connection, CLEAR_SQL)) {
            int count = clearStatement.executeUpdate();
            log.info("Map '{}' cleared, {} rows deleted", this.currentMapName, count);
        }
    }

    private PreparedStatement withMapName(Connection connection, String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, this.currentMapName);
        return statement;
    }

    private PreparedStatement withMapNameKey(Connection connection, String sql, String key) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, this.currentMapName);
        statement.setString(2, key);
        return statement;
    }

    private PreparedStatement withMapNameKeyValue(Connection connection, String sql, String key, String value)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, this.currentMapName);
        statement.setString(2, key);
        statement.setString(3, value);
        return statement;
    }

    private void checkMapIsInitialized() {
        if (this.currentMapName == null) {
            throw new IllegalStateException("Map is not initialized. Call method init() before using the map.");
        }
    }
}
