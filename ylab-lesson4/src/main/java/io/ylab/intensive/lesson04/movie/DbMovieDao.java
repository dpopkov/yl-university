package io.ylab.intensive.lesson04.movie;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.stream.IntStream;

public class DbMovieDao {

    private static final String INSERT_SQL = ""
            + "INSERT INTO movie "
            + "(year, length, title, subject, actors, actress, director, popularity, awards) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final DataSource dataSource;

    public DbMovieDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveAll(List<Movie> movies) {
        try (Connection connection = this.dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(INSERT_SQL)) {
                ParameterSetter setter = new ParameterSetter();
                for (Movie movie : movies) {
                    setter.reset();
                    setter.setInteger(ps, movie.getYear());
                    setter.setInteger(ps, movie.getLength());
                    setter.setString(ps, movie.getTitle());
                    setter.setString(ps, movie.getSubject());
                    setter.setString(ps, movie.getActors());
                    setter.setString(ps, movie.getActress());
                    setter.setString(ps, movie.getDirector());
                    setter.setInteger(ps, movie.getPopularity());
                    setter.setBoolean(ps, movie.getAwards());
                    ps.addBatch();
                }
                int[] insertCounts = ps.executeBatch();
                connection.commit();
                return IntStream.of(insertCounts).summaryStatistics().getSum();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException sqlEx) {
            throw new MovieException("Ошибка при сохранении Movie в базу данных", sqlEx);
        }
    }

    private static class ParameterSetter {
        private int parameter = 1;

        void reset() {
            this.parameter = 1;
        }

        void setInteger(PreparedStatement ps, Integer value) throws SQLException {
            if (value != null) {
                ps.setInt(this.parameter++, value);
            } else {
                ps.setNull(this.parameter++, Types.INTEGER);
            }
        }

        void setString(PreparedStatement ps, String value) throws SQLException {
            if (value != null) {
                ps.setString(this.parameter++, value);
            } else {
                ps.setNull(this.parameter++, Types.VARCHAR);
            }
        }

        void setBoolean(PreparedStatement ps, Boolean value) throws SQLException {
            if (value != null) {
                ps.setBoolean(this.parameter++, value);
            } else {
                ps.setNull(this.parameter++, Types.BOOLEAN);
            }
        }
    }
}
