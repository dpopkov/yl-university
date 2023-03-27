package io.ylab.intensive.lesson04.filesort;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

interface DbInserter {

    void insertNumbers(Connection openConnection, List<Long> numbers) throws SQLException;
}
