package io.ylab.intensive.lesson04.persistentmap;

import io.ylab.intensive.lesson04.DbUtil;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class PersistentMapImplTest {

    private static final String K_1 = "key1";
    private static final String K_2 = "key2";
    private static final String V_1 = "value1";
    private static final String V_2 = "value2";
    private static final String MAP_1 = "map1";
    private static final String MAP_2 = "map2";

    private PersistentMapImpl map;

    @BeforeEach
    void setUp() throws SQLException {
        map = new PersistentMapImpl(initH2InMemoryDataSource());
    }

    private JdbcDataSource initH2InMemoryDataSource() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1;NON_KEYWORDS=KEY,VALUE");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        String createMapTable = ""
                + "CREATE TABLE if not exists persistent_map (\n"
                + "   map_name varchar,\n"
                + "   KEY varchar,\n"
                + "   value varchar\n"
                + ");";
        DbUtil.applyDdl(createMapTable, dataSource);
        return dataSource;
    }

    @Test
    void testPut_whenPutNewKeys_thenContainsKeysAndValues() throws SQLException {
        map.init(MAP_1);
        assertThat(map.containsKey(K_1)).isFalse();
        assertThat(map.containsKey(K_2)).isFalse();
        assertThat(map.get(K_1)).isNull();
        assertThat(map.get(K_2)).isNull();

        map.put(K_1, V_1);
        assertThat(map.containsKey(K_1)).isTrue();
        assertThat(map.get(K_1)).isEqualTo(V_1);
        assertThat(map.get(K_2)).isNull();
        map.put(K_2, V_2);
        assertThat(map.containsKey(K_2)).isTrue();
        assertThat(map.get(K_2)).isEqualTo(V_2);
        assertThat(map.getKeys())
                .hasSize(2)
                .contains(K_1, K_2);

        map.init(MAP_2);
        assertThat(map.containsKey(K_1)).isFalse();
        map.put(K_1, V_1);
        assertThat(map.containsKey(K_1)).isTrue();
        assertThat(map.get(K_1)).isEqualTo(V_1);
        assertThat(map.getKeys())
                .hasSize(1)
                .contains(K_1);
    }

    @Test
    void testPut_whenPutOldKeysWithNewValues_thenUpdatesValues() throws SQLException {
        map.init(MAP_1);
        map.put(K_1, V_1);
        map.put(K_2, V_2);
        assertThat(map.get(K_1)).isEqualTo(V_1);
        assertThat(map.get(K_2)).isEqualTo(V_2);

        map.put(K_1, V_2);
        map.put(K_2, V_1);
        assertThat(map.get(K_1)).isEqualTo(V_2);
        assertThat(map.get(K_2)).isEqualTo(V_1);
    }

    @Test
    void testRemove_whenRemove_thenDoesNotContainKeys() throws SQLException {
        map.init(MAP_1);
        map.put(K_1, V_1);
        map.put(K_2, V_2);
        assertThat(map.get(K_1)).isEqualTo(V_1);
        assertThat(map.get(K_2)).isEqualTo(V_2);

        map.remove(K_1);
        assertThat(map.containsKey(K_1)).isFalse();
        assertThat(map.get(K_1)).isNull();
        assertThat(map.containsKey(K_2)).isTrue();
        assertThat(map.get(K_2)).isEqualTo(V_2);

        map.remove(K_2);
        assertThat(map.containsKey(K_2)).isFalse();
        assertThat(map.get(K_2)).isNull();
    }

    @Test
    void testClear_whenClear_thenMapDoesNotContainAnyData() throws SQLException {
        map.init(MAP_1);
        map.put(K_1, V_1);
        map.put(K_2, V_2);
        assertThat(map.get(K_1)).isEqualTo(V_1);
        assertThat(map.get(K_2)).isEqualTo(V_2);

        map.init(MAP_2);
        map.put(K_1, V_1);
        map.put(K_2, V_2);
        assertThat(map.get(K_1)).isEqualTo(V_1);
        assertThat(map.get(K_2)).isEqualTo(V_2);

        map.init(MAP_1);
        assertThat(map.containsKey(K_1)).isTrue();
        assertThat(map.containsKey(K_2)).isTrue();
        map.clear();
        assertThat(map.containsKey(K_1)).isFalse();
        assertThat(map.containsKey(K_2)).isFalse();
        assertThat(map.get(K_1)).isNull();
        assertThat(map.get(K_2)).isNull();

        map.init(MAP_2);
        assertThat(map.containsKey(K_1)).isTrue();
        assertThat(map.containsKey(K_2)).isTrue();
        map.clear();
        assertThat(map.containsKey(K_1)).isFalse();
        assertThat(map.containsKey(K_2)).isFalse();
        assertThat(map.get(K_1)).isNull();
        assertThat(map.get(K_2)).isNull();
    }
}
