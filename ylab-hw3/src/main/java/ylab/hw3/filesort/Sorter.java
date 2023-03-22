package ylab.hw3.filesort;

import ylab.hw3.filesort.io.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Sorter {
    private static final int DEFAULT_MAX_TO_SORT_IN_MEMORY = 100_000;

    private final int maxNumbersToSortInMemory;

    public Sorter() {
        this(DEFAULT_MAX_TO_SORT_IN_MEMORY);
    }

    public Sorter(int maxNumbersToSortInMemory) {
        this.maxNumbersToSortInMemory = maxNumbersToSortInMemory;
    }

    /**
     * Получает на вход файл с числами и возвращает отсортированный по возрастанию файл.
     * @param dataFile исходный файл с неотсортированными числами
     * @return отсортированный по возрастанию файл
     */
    public File sortFile(File dataFile) throws IOException {
        Queue<File> temporaryFiles = splitDataToTemporarySortedFiles(dataFile);
        File resultFile = new File(dataFile.getAbsolutePath() + ".sorted");
        return mergeAll(temporaryFiles, resultFile);
    }

    private Queue<File> splitDataToTemporarySortedFiles(File dataFile) throws IOException {
        Queue<File> temporaryFiles = new ArrayDeque<>();
        List<Long> buffer = new ArrayList<>();
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(dataFile));
             Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLong()) {
                int count = 0;
                buffer.clear();
                while (count < maxNumbersToSortInMemory && scanner.hasNextLong()) {
                    buffer.add(scanner.nextLong());
                    count++;
                }
                if (!buffer.isEmpty()) {
                    Collections.sort(buffer);
                    File temporary = LongToTemporaryWriter.writeAllLongs(buffer);
                    temporaryFiles.offer(temporary);
                }
            }
        }
        return temporaryFiles;
    }

    private File mergeAll(Queue<File> queue, File pathToResultFile) {
        if (queue.isEmpty()) {
            throw new FileSortException("Отсутствуют файлы для слияния");
        } else if (queue.size() == 1) {
            return copySingleFile(queue.poll(), pathToResultFile);
        } else if (queue.size() == 2) {
            return mergeToResult(queue.poll(), queue.poll(), pathToResultFile);
        }
        while (queue.size() > 2) {
            File merged = mergeToTemporary(queue.poll(), queue.poll());
            queue.offer(merged);
        }
        return mergeToResult(queue.poll(), queue.poll(), pathToResultFile);
    }

    private File copySingleFile(File input, File pathToResultFile) {
        try {
            return Files.copy(input.toPath(), pathToResultFile.toPath()).toFile();
        } catch (IOException ex) {
            throw new FileSortException("Ошибка при копировании результата", ex);
        }
    }

    private File mergeToTemporary(File a, File b) {
        try (LongReader readerA = new LongReader(a);
             LongReader readerB = new LongReader(b);
             LongWriter writer = new LongToTemporaryWriter()) {
            merge(readerA, readerB, writer);
            return writer.getFile();
        } catch (Exception ex) {
            throw new FileSortException("Ошибка при слиянии файлов", ex);
        }
    }

    private File mergeToResult(File a, File b, File pathToResultFile) {
        try (LongReader readerA = new LongReader(a);
             LongReader readerB = new LongReader(b);
             LongWriter writer = new LongNormalWriter(pathToResultFile)) {
            merge(readerA, readerB, writer);
            return writer.getFile();
        } catch (Exception ex) {
            throw new FileSortException("Ошибка при слиянии файлов", ex);
        }
    }

    private void merge(LongReader readerA, LongReader readerB, LongWriter writer) {
        long aValue;
        long bValue;
        while (readerA.hasNext() && readerB.hasNext()) {
            aValue = readerA.peekLong();
            bValue = readerB.peekLong();
            if (aValue < bValue) {
                aValue = readerA.nextLong();
                writer.write(aValue);
            } else {
                bValue = readerB.nextLong();
                writer.write(bValue);
            }
        }
        while (readerA.hasNext()) {
            aValue = readerA.nextLong();
            writer.write(aValue);
        }
        while (readerB.hasNext()) {
            bValue = readerB.nextLong();
            writer.write(bValue);
        }
    }
}
