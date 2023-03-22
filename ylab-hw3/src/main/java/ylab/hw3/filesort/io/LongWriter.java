package ylab.hw3.filesort.io;

import java.io.File;

public interface LongWriter extends AutoCloseable {

    void write(long value);
    File getFile();
}
