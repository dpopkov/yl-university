package io.ylab.intensive.lesson04.movie;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieFileReader {
    private static final int DEFAULT_NUMBER_OF_LINES_TO_SKIP = 2;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;
    private static final int MINIMAL_NUMBER_OF_FIELDS = 9;

    private final File inputFile;
    private final int linesToSkip;
    private final Charset charset;

    public MovieFileReader(File inputFile) {
        this(inputFile, DEFAULT_NUMBER_OF_LINES_TO_SKIP, DEFAULT_CHARSET);
    }

    public MovieFileReader(File inputFile, int linesToSkip, Charset charset) {
        if (!inputFile.exists()) {
            throw new MovieException("Файл не найден: " + inputFile);
        }
        this.inputFile = inputFile;
        this.linesToSkip = linesToSkip;
        this.charset = charset;
    }

    public List<Movie> readAll() {
        List<Movie> movies = new ArrayList<>();
        try (Scanner scanner = new Scanner(this.inputFile, this.charset)) {
            skipLines(scanner);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Movie movie = parseMovieLine(line);
                movies.add(movie);
            }
        } catch (Exception e) {
            throw new MovieException("Ошибка при чтении файла " + this.inputFile, e);
        }
        return movies;
    }

    private void skipLines(Scanner scanner) {
        int count = 0;
        while (count < this.linesToSkip && scanner.hasNextLine()) {
            scanner.nextLine();
            count++;
        }
        if (count != this.linesToSkip) {
            throw new MovieException("Файл содержит недостаточное количество строк.");
        }
    }

    private Movie parseMovieLine(String line) {
        String[] tokens = line.split(";");
        if (tokens.length < MINIMAL_NUMBER_OF_FIELDS) {
            throw new IllegalArgumentException("Количество полей в записи недостаточно.");
        }
        final Movie movie = new Movie();
        int fieldIdx = 0;
        movie.setYear(parseNullableIntegerValue(tokens[fieldIdx++]));
        movie.setLength(parseNullableIntegerValue(tokens[fieldIdx++]));
        movie.setTitle(nullableStringValue(tokens[fieldIdx++]));
        movie.setSubject(nullableStringValue(tokens[fieldIdx++]));
        movie.setActors(nullableStringValue(tokens[fieldIdx++]));
        movie.setActress(nullableStringValue(tokens[fieldIdx++]));
        movie.setDirector(nullableStringValue(tokens[fieldIdx++]));
        movie.setPopularity(parseNullableIntegerValue(tokens[fieldIdx++]));
        movie.setAwards(parseNullableBooleanValue(tokens[fieldIdx]));
        return movie;
    }

    private Integer parseNullableIntegerValue(String integerString) {
        if (integerString.isBlank()) {
            return null;
        }
        Integer integerValue;
        try {
            integerValue = Integer.valueOf(integerString);
        } catch (NumberFormatException nfException) {
            integerValue = null;
        }
        return integerValue;
    }

    private String nullableStringValue(String s) {
        if (s.isBlank()) {
            return null;
        }
        return s;
    }

    private Boolean parseNullableBooleanValue(String booleanString) {
        if (booleanString.isBlank()) {
            return null;
        }
        return Boolean.valueOf(booleanString);
    }
}
