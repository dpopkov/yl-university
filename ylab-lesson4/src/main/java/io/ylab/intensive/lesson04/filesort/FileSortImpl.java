package io.ylab.intensive.lesson04.filesort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.sql.DataSource;

public class FileSortImpl implements FileSorter {

    private final DataSource dataSource;
    private final DbInserter dbInserter;

    public FileSortImpl(DataSource dataSource, DbInserter dbInserter) {
        this.dataSource = dataSource;
        this.dbInserter = dbInserter;
    }

    @Override
    public File sort(File data) {
        List<Long> nonSortedNumbers = readLongValues(data);
        List<Long> sortedNumbers = sortWithDb(nonSortedNumbers);
        return writeLongValues(sortedNumbers, new File(data.getAbsolutePath() + ".sorted"));
    }

    private List<Long> sortWithDb(List<Long> nonSortedNumbers) {
        try (Connection connection = this.dataSource.getConnection()) {
            insertNumbersToDb(nonSortedNumbers, connection);
            return readFromDbOrderedDescending(connection);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx.getMessage());
            return List.of();
        }
    }

    private List<Long> readFromDbOrderedDescending(Connection connection) throws SQLException {
        List<Long> sortedValues = new ArrayList<>();
        try (Statement selectStatement = connection.createStatement()) {
            ResultSet resultSet = selectStatement.executeQuery(SqlStatements.SELECT_NUMBERS_ORDERED_DESC);
            while (resultSet.next()) {
                sortedValues.add(resultSet.getLong(1));
            }
        }
        return sortedValues;
    }

    private void insertNumbersToDb(List<Long> nonSortedNumbers, Connection connection) throws SQLException {
        System.out.println("Начинаем вставку используя " + this.dbInserter.getClass().getSimpleName());
        long started = System.currentTimeMillis();
        this.dbInserter.insertNumbers(connection, nonSortedNumbers);
        long elapsedMilliseconds = System.currentTimeMillis() - started;
        System.out.println("Вставка завершена. На вставку чисел ушло " + elapsedMilliseconds + " ms.");
    }

    private List<Long> readLongValues(File data) {
        List<Long> longValues = new ArrayList<>();
        try (Scanner scanner = new Scanner(data)) {
            while (scanner.hasNextLong()) {
                longValues.add(scanner.nextLong());
            }
            System.out.println("Завершено чтение " + longValues.size() + " значений.");
        } catch (IOException ex) {
            System.err.println("Ошибка при открытии входного файла данных: " + ex.getMessage());
        }
        return longValues;
    }

    private File writeLongValues(List<Long> sortedValues, File targetFile) {
        try {
            PrintWriter writer = new PrintWriter(targetFile);
            for (Long value : sortedValues) {
                writer.println(value);
            }
            writer.flush();
        } catch (FileNotFoundException ex) {
            System.err.println("Ошибка создания файла для сохранения чисел: " + ex.getMessage());
        }
        return targetFile;
    }
}
