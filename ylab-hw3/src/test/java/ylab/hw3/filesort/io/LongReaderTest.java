package ylab.hw3.filesort.io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class LongReaderTest {

    @Test
    void whenPeekLongAndNextLongRepeatedly_thenBehavesCorrect() throws Exception {
        URL url = LongReaderTest.class.getResource("long-reader-data.txt");
        File file = new File(url.toURI());
        try (LongReader reader = new LongReader(file)) {
            assertTrue(reader.hasNext());
            long one = reader.peekLong();
            assertEquals(1L, one);

            assertTrue(reader.hasNext());
            one = reader.peekLong();
            assertEquals(1L, one);

            assertTrue(reader.hasNext());
            one = reader.nextLong();
            assertEquals(1L, one);

            assertTrue(reader.hasNext());
            long two = reader.peekLong();
            assertEquals(2L, two);

            assertTrue(reader.hasNext());
            two = reader.peekLong();
            assertEquals(2L, two);

            assertTrue(reader.hasNext());
            two = reader.nextLong();
            assertEquals(2L, two);

            assertFalse(reader.hasNext());
        }
    }
}
