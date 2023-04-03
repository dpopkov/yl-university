package io.ylab.intensive.lesson05.messagefilter.io;

import java.io.*;

/**
 * Данный класс используется для подготовки данных и напрямую к заданию не относится.
 */
public class XorFileInputStream extends FilterInputStream {

    private static final int DEFAULT_KEY = 41;
    private static final int END_OF_DATA = -1;

    private final int key;

    public XorFileInputStream(File file) throws FileNotFoundException {
        this(file, DEFAULT_KEY);
    }

    public XorFileInputStream(File file, int key) throws FileNotFoundException {
        super(new BufferedInputStream(new FileInputStream(file)));
        this.key = key;
    }

    @Override
    public int read() throws IOException {
        int byteValue = super.read();
        return byteValue != END_OF_DATA ? byteValue ^ this.key : byteValue;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int result = in.read(b, off, len);
        if (result == END_OF_DATA) {
            return END_OF_DATA;
        }
        for (int i = off; i < len; i++) {
            b[i] = (byte) (b[i] ^ this.key);
        }
        return result;
    }
}
