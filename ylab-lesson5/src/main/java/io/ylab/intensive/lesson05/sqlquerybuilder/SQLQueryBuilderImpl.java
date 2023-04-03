package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {
    /** Значение для получения из DatabaseMetaData списка таблиц всех существующих типов.  */
    private static final String[] ALL_TABLE_TYPES = null;
    /** Значение для параметров, которые не используются при получении данных из DatabaseMetaData. */
    private static final String NOT_USED = null;
    private static final String RESULT_SET_LABEL_FOR_TABLE_NAME = "TABLE_NAME";
    private static final String RESULT_SET_LABEL_FOR_COLUMN_NAME = "COLUMN_NAME";

    private final DataSource dataSource;

    public SQLQueryBuilderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String queryForTable(String tableName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            if (notFoundInAllTables(tableName)) {
                return null;
            }
            DatabaseMetaData metaData = connection.getMetaData();
            List<String> columnNames = getColumnNames(metaData, tableName.toLowerCase());
            return buildSelectStatement(columnNames, tableName);
        }
    }

    @Override
    public List<String> getTables() throws SQLException {
        List<String> tableNames = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getTables(NOT_USED, NOT_USED, NOT_USED, ALL_TABLE_TYPES)) {
                while (resultSet.next()) {
                    tableNames.add(resultSet.getString(RESULT_SET_LABEL_FOR_TABLE_NAME));
                }
            }
        }
        return tableNames;
    }

    private boolean notFoundInAllTables(String tableName) throws SQLException {
        List<String> allTables = getTables();
        for (String table : allTables) {
            if (table.equalsIgnoreCase(tableName)) {
                return false;
            }
        }
        return true;
    }

    private List<String> getColumnNames(DatabaseMetaData metaData, String tableName) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        tryToGetColumnNamesForLiterateTableName(metaData, columnNames, tableName.toLowerCase());
        if (columnNames.isEmpty()) {
            tryToGetColumnNamesForLiterateTableName(metaData, columnNames, tableName.toUpperCase());
        }
        return columnNames;
    }

    private void tryToGetColumnNamesForLiterateTableName(DatabaseMetaData metaData, List<String> columnNames,
                                                         String tableNamePattern) throws SQLException {
        try (ResultSet resultSet = metaData.getColumns(NOT_USED, NOT_USED, tableNamePattern, NOT_USED)) {
            while (resultSet.next()) {
                columnNames.add(resultSet.getString(RESULT_SET_LABEL_FOR_COLUMN_NAME));
            }
        }
    }

    private String buildSelectStatement(List<String> columnNames, String tableName) {
        if (columnNames.isEmpty()) {
            System.err.println("Не могу построить запрос из-за отсутствия имен колонок для " + tableName);
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        stringBuilder.append(columnNames.get(0));
        for (int i = 1; i < columnNames.size(); i++) {
            stringBuilder.append(", ").append(columnNames.get(i));
        }
        stringBuilder.append(" FROM ").append(tableName);
        return stringBuilder.toString();
    }
}
