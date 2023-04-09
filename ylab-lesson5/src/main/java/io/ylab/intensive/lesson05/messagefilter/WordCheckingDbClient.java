package io.ylab.intensive.lesson05.messagefilter;

import io.ylab.intensive.lesson05.DbUtil;
import io.ylab.intensive.lesson05.messagefilter.io.FilteredWordsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@Component
public class WordCheckingDbClient {
    private static final String MESSAGE_FILTER_WORDS_TABLE = "message_filter_words";
    private static final String SELECT_SQL = "SELECT filter_word FROM " + MESSAGE_FILTER_WORDS_TABLE + " WHERE filter_word = ?";
    private static final String TRUNCATE_SQL = "TRUNCATE TABLE " + MESSAGE_FILTER_WORDS_TABLE;
    private static final String INSERT_SQL = "INSERT INTO " + MESSAGE_FILTER_WORDS_TABLE + "(filter_word) VALUES(?)";
    private static final Logger log = LoggerFactory.getLogger(WordCheckingDbClient.class);

    private final DataSource dataSource;
    private Connection longConnection;
    private PreparedStatement selectStatement;
    private boolean isReadyForCheckingWords;

    public WordCheckingDbClient(DataSource dataSource, FilteredWordsProvider wordsProvider) throws SQLException, IOException {
        this.dataSource = dataSource;
        initTable(wordsProvider.readAllWords());
    }

    public void startConnection() throws SQLException {
        if (this.longConnection == null || this.longConnection.isClosed()) {
            this.longConnection = dataSource.getConnection();
            this.selectStatement = this.longConnection.prepareStatement(SELECT_SQL);
        }
        isReadyForCheckingWords = true;
    }

    public boolean containsWord(String word) throws SQLException {
        if (!isReadyForCheckingWords) {
            throw new IllegalStateException("DbClient не готов к работе.");
        }
        log.trace("Поиск слова '{}'", word);
        this.selectStatement.setString(1, word);
        try (ResultSet resultSet = this.selectStatement.executeQuery()) {
            boolean found = resultSet.next();
            if (found) {
                log.debug("Слово '{}' найдено.", word);
            }
            return found;
        }
    }

    public void stopConnection() throws SQLException {
        if (this.longConnection != null && !this.longConnection.isClosed()) {
            this.longConnection.close();
        }
        isReadyForCheckingWords = false;
    }

    @PreDestroy
    public void close() throws SQLException {
        log.info("Завершение работы db клиента.");
        stopConnection();
    }

    private void initTable(List<String> words) throws SQLException {
        if (words.isEmpty()) {
            log.error("Список слов для проверки пуст. DB клиент не готов к работе.");
            return;
        }
        try (Connection shortConnection = this.dataSource.getConnection()) {
            if (tableExists(shortConnection)) {
                clearTable(shortConnection);
            } else {
                createTable(shortConnection);
            }
            loadData(shortConnection, words);
        }
    }

    private boolean tableExists(Connection openConnection) throws SQLException {
        DatabaseMetaData metaData = openConnection.getMetaData();
        try (ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"})) {
            while (resultSet.next()) {
                String table = resultSet.getString("TABLE_NAME");
                if (MESSAGE_FILTER_WORDS_TABLE.equalsIgnoreCase(table)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void createTable(Connection openConnection) throws SQLException {
        String ddl = ""
                + "create table if not exists " + MESSAGE_FILTER_WORDS_TABLE + " (\n"
                + "filter_word varchar\n"
                + ")";
        DbUtil.applyDdl(ddl, openConnection);
        log.info("Таблица {} создана.", MESSAGE_FILTER_WORDS_TABLE);
    }

    private void clearTable(Connection openConnection) throws SQLException {
        Statement statement = openConnection.createStatement();
        statement.execute(TRUNCATE_SQL);
        log.info("Таблица {} очищена.", MESSAGE_FILTER_WORDS_TABLE);
    }

    private void loadData(Connection openConnection, List<String> words) throws SQLException {
        openConnection.setAutoCommit(false);
        try (PreparedStatement insertStatement = openConnection.prepareStatement(INSERT_SQL)) {
            for (String word : words) {
                insertStatement.setString(1, word.toLowerCase());
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
            log.info("Загружено {} слов.", words.size());
        } finally {
            openConnection.setAutoCommit(true);
        }
    }
}
