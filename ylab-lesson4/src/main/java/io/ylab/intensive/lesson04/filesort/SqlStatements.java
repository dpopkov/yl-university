package io.ylab.intensive.lesson04.filesort;

class SqlStatements {
    static final String INSERT_NUMBERS = "INSERT INTO numbers (val) VALUES (?)";
    static final String SELECT_NUMBERS_ORDERED_DESC = "SELECT val FROM numbers ORDER BY val DESC";
}
