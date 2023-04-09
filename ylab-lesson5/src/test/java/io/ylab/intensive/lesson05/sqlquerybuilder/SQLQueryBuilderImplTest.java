package io.ylab.intensive.lesson05.sqlquerybuilder;

import io.ylab.intensive.lesson05.DbUtil;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class SQLQueryBuilderImplTest {

    private SQLQueryBuilderImpl queryBuilder;

    @BeforeEach
    void setUp() throws SQLException {
        queryBuilder = new SQLQueryBuilderImpl(initH2InMemoryDataSource());
    }

    private JdbcDataSource initH2InMemoryDataSource() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1;NON_KEYWORDS=KEY,VALUE");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        String createMapTable = ""
                + "CREATE TABLE if not exists test_table_1 (\n"
                + "   field1 varchar,\n"
                + "   field2 varchar\n"
                + ");"
                + "CREATE TABLE if not exists test_table_2 (\n"
                + "   field1 varchar\n"
                + ");";
        DbUtil.applyDdl(createMapTable, dataSource);
        return dataSource;
    }

    @Test
    void testGetTables_whenGetTables_thenReturnsTableNames() throws SQLException {
        List<String> tableNames = queryBuilder.getTables().stream()
                .map(String::toLowerCase).collect(Collectors.toList());
        assertThat(tableNames)
                .contains("test_table_1")
                .contains("test_table_2");
    }

    @Test
    void testQueryForTable_whenQueryForExistingTable_thenReturnsSelectQueryContainingAllColumns() throws SQLException {
        String query1 = queryBuilder.queryForTable("test_table_1");
        assertThat(query1).isEqualToIgnoringCase("SELECT field1, field2 FROM test_table_1");

        String query2 = queryBuilder.queryForTable("test_table_2");
        assertThat(query2).isEqualToIgnoringCase("SELECT field1 FROM test_table_2");
    }

    @Test
    void testQueryForTable_whenQueryForNonExistingTable_thenReturnsNull() throws SQLException {
        String query = queryBuilder.queryForTable("non_existing_table");
        assertThat(query).isNull();
    }
}
