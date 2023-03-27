package io.ylab.intensive.lesson04.filesort;

import java.io.File;
import java.sql.SQLException;
import javax.sql.DataSource;

import io.ylab.intensive.lesson04.DbUtil;

public class FileSorterTest {
    public static void main(String[] args) throws SQLException {
        final DataSource dataSource = initDb();
        final String filename = args.length > 0 ? args[0] : "data.txt";
        final File data = new File(filename);
        final String mode = args.length > 1 ? args[1] : "batch";
        /* Использование batch-processing ускорило вставку примерно в 37 раз (10000 записей). */
        final DbInserter dbInserter = "batch".equalsIgnoreCase(mode)
                ? new DbInserterWithBatch()
                : new DbInserterSlow();

        FileSorter fileSorter = new FileSortImpl(dataSource, dbInserter);
        File result = fileSorter.sort(data);
        System.out.println("Результат сохранен в " + result);
    }

    public static DataSource initDb() throws SQLException {
        String createSortTable = ""
                + "drop table if exists numbers;"
                + "create table if not exists numbers (\n"
                + "\tval bigint\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createSortTable, dataSource);
        return dataSource;
    }
}
