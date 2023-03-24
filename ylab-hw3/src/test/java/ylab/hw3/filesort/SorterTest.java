package ylab.hw3.filesort;

import org.junit.jupiter.api.Test;
import ylab.hw3.filesort.io.LongReader;
import ylab.hw3.filesort.io.LongToTemporaryWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SorterTest {

    public static final int NUM_OF_LONGS = 100;

    @Test
    void testSortFile() throws Exception {
        List<Long> data = makeContiguousNumbers(NUM_OF_LONGS);
        Collections.shuffle(data);
        Sorter sorter = new Sorter(30);

        File inputFile = LongToTemporaryWriter.writeAllLongs(data);
        File sortedFile = sorter.sortFile(inputFile);
        List<Long> numbers = readToList(sortedFile);
        assertEquals(NUM_OF_LONGS, numbers.size());
        for (long i = 1L; i <= numbers.size(); i++) {
            assertEquals(i, numbers.get((int)(i - 1)));
        }
        assertTrue(sortedFile.delete());
    }

    private List<Long> makeContiguousNumbers(int size) {
        List<Long> list = new ArrayList<>();
        for (long i = 1L; i <= size; i++) {
            list.add(i);
        }
        return list;
    }

    private List<Long> readToList(File file) throws Exception {
        List<Long> numbers = new ArrayList<>();
        try (LongReader reader = new LongReader(file)) {
            while (reader.hasNext()) {
                numbers.add(reader.nextLong());
            }
        }
        return numbers;
    }
}
