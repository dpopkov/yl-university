package io.ylab.intensive.lesson04.persistentmap;

import io.ylab.intensive.lesson04.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class PersistenceMapTest {
    private final static Logger log = LoggerFactory.getLogger(PersistenceMapTest.class);

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = initDb();

        PersistentMap demoMap = new PersistentMapImpl(dataSource);
        demoMap.init("demo");
        demoMap.put("Doe", "John");
        display(demoMap);
        demoMap.put("Doe", "Jane");
        display(demoMap);
        demoMap.remove("Doe");
        display(demoMap);

        PersistentMap mapToClear = new PersistentMapImpl(dataSource);
        mapToClear.init("mapToClear");
        mapToClear.put("Sparrow", "Jack");
        mapToClear.put("Gibbs", "Joshamee");
        display(mapToClear);
        mapToClear.clear();
        display(mapToClear);
    }

    private static void display(PersistentMap map) {
        try {
            List<String> keys = map.getKeys();
            if (keys.isEmpty()) {
                System.out.println("PersistentMap пуста.");
            } else {
                for (String key : keys) {
                    System.out.printf("key: %s, value: %s%n", key, map.get(key));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public static DataSource initDb() throws SQLException {
        String createMapTable = ""
                + "drop table if exists persistent_map; "
                + "CREATE TABLE if not exists persistent_map (\n"
                + "   map_name varchar,\n"
                + "   KEY varchar,\n"
                + "   value varchar\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createMapTable, dataSource);
        return dataSource;
    }
}
