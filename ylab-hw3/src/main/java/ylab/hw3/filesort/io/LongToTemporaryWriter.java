package ylab.hw3.filesort.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class LongToTemporaryWriter extends LongNormalWriter {
    private static final String PREFIX = "filesort-";
    private static final String SUFFIX = "-long.tmp";

    public LongToTemporaryWriter() throws IOException {
        super(createTemporary());
    }

    public static File writeAllLongs(List<Long> values) throws IOException {
        File temporarySorted = createTemporary();
        try (PrintWriter writer = new PrintWriter(temporarySorted)) {
            for (Long value : values) {
                writer.println(value);
            }
            writer.flush();
        }
        return temporarySorted;
    }

    private static File createTemporary() throws IOException {
        File file = File.createTempFile(PREFIX, SUFFIX);
        file.deleteOnExit();
        return file;
    }
}
