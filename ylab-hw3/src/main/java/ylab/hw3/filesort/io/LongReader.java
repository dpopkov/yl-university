package ylab.hw3.filesort.io;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LongReader implements AutoCloseable {

    private final Scanner scanner;
    private boolean hasPeeked;
    private long peekedValue;

    public LongReader(File file) throws IOException {
        this.scanner = new Scanner(file);
    }

    public boolean hasNext() {
        return hasPeeked || scanner.hasNextLong();
    }

    public long peekLong() {
        if (!hasPeeked) {
            peekedValue = scanner.nextLong();
            hasPeeked = true;
        }
        return peekedValue;
    }

    public long nextLong() {
        if (hasPeeked) {
            hasPeeked = false;
            return peekedValue;
        } else {
            return scanner.nextLong();
        }
    }

    @Override
    public void close() {
        this.scanner.close();
    }
}
