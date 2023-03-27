package io.ylab.intensive.lesson04.filesort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

class DbInserterSlow implements DbInserter {

    @Override
    public void insertNumbers(Connection openConnection, List<Long> numbers) throws SQLException {
        try (PreparedStatement insertStatement = openConnection.prepareStatement(SqlStatements.INSERT_NUMBERS)) {
            for (Long value : numbers) {
                insertStatement.setLong(1, value);
                insertStatement.executeUpdate();
            }
        }
    }
}
