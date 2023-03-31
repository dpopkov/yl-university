package io.ylab.intensive.lesson04.movie;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;

public class MovieLoaderImpl implements MovieLoader {
    private final DataSource dataSource;

    public MovieLoaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void loadData(File file) {
        MovieFileReader movieReader = new MovieFileReader(file);
        List<Movie> movies = movieReader.readAll();
        if (!movies.isEmpty()) {
            DbMovieDao movieDao = new DbMovieDao(this.dataSource);
            long numInserts = movieDao.saveAll(movies);
            System.out.println("Сохранено " + numInserts + " объектов");
        }
    }
}
