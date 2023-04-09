package io.ylab.intensive.lesson05.messagefilter.io;

import io.ylab.intensive.lesson05.messagefilter.MessageFilterProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class FilteredWordsProvider {
    private static final Logger log = LoggerFactory.getLogger(FilteredWordsProvider.class);

    private final MessageFilterProps messageFilterProps;

    public FilteredWordsProvider(MessageFilterProps messageFilterProps) {
        this.messageFilterProps = messageFilterProps;
    }

    public List<String> readAllWords() throws IOException {
        String pathToFile = this.messageFilterProps.getFilterWordsPath();
        Path path = Path.of(pathToFile);
        if (!Files.exists(path)) {
            throw new IllegalStateException("Файл не существует " + pathToFile);
        }
        String mode = this.messageFilterProps.getFilterWordsMode().toLowerCase();
        if ("plain-text".equals(mode)) {
            return readLinesFromPlainTextFile(path);
        } else if ("xored-text".equals(mode)) {
            return readXoredFile(pathToFile);
        } else {
            throw new IllegalStateException("Неизвестный режим чтения: " + mode);
        }
    }

    private List<String> readXoredFile(String pathToFile) throws IOException {
        String unXored = FileUtils.replaceExtension(pathToFile, ".xored.bin", ".txt");
        File unXoredFile = new File(unXored);
        if (!unXoredFile.exists()) {
            FileUtils.copyWithXorOperation(new File(pathToFile), unXoredFile);
            log.info("Создан файл {}", unXoredFile);
        }
        return readLinesFromPlainTextFile(unXoredFile.toPath());
    }

    private List<String> readLinesFromPlainTextFile(Path path) throws IOException {
        log.info("Загрузка слов из файла {}.", path);
        return Files.readAllLines(path);
    }
}
