package ylab.hw3.filesort.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class LongNormalWriter implements LongWriter {
    private final File file;
    private final PrintWriter writer;

    public LongNormalWriter(File file) throws IOException {
        this.file = file;
        this.writer = new PrintWriter(file);
    }

    @Override
    public void write(long value) {
        writer.println(value);
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void close() {
        writer.close();
    }
}
