package ylab.hw2.sequences;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequenceGeneratorImplTest {

    private ByteArrayOutputStream buffer;
    private SequenceGeneratorImpl generator;

    @BeforeEach
    void setUpSystemOut() {
        buffer = new ByteArrayOutputStream();
        generator = new SequenceGeneratorImpl(new PrintStream(buffer));
    }

    @Test
    void testA() {
        long[] numbers = {2, 4, 6, 8, 10};
        generator.a(numbers.length);
        assertEquals(expectedResult(numbers), actualResult());
    }

    @Test
    void testB() {
        long[] numbers = {1, 3, 5, 7, 9};
        generator.b(numbers.length);
        assertEquals(expectedResult(numbers), actualResult());
    }

    @Test
    void testC() {
        long[] numbers = {1, 4, 9, 16, 25};
        generator.c(numbers.length);
        assertEquals(expectedResult(numbers), actualResult());
    }

    @Test
    void testD() {
        long[] numbers = {1, 8, 27, 64, 125};
        generator.d(numbers.length);
        assertEquals(expectedResult(numbers), actualResult());
    }

    @Test
    void testE() {
        long[] numbers = {1, -1, 1, -1, 1, -1};
        generator.e(numbers.length);
        assertEquals(expectedResult(numbers), actualResult());
    }

    @Test
    void testF() {
        long[] numbers = {1, -2, 3, -4, 5, -6};
        generator.f(numbers.length);
        assertEquals(expectedResult(numbers), actualResult());
    }

    @Test
    void testG() {
        long[] numbers = {1, -4, 9, -16, 25};
        generator.g(numbers.length);
        assertEquals(expectedResult(numbers), actualResult());
    }

    @Test
    void testH() {
        long[] numbers = {1, 0, 2, 0, 3, 0, 4};
        generator.h(numbers.length);
        assertEquals(expectedResult(numbers), actualResult());
    }

    @Test
    void testI() {
        long[] numbers = {1, 2, 6, 24, 120, 720};
        generator.i(numbers.length);
        assertEquals(expectedResult(numbers), actualResult());
    }

    @Test
    void testJ() {
        long[] numbers = {1, 1, 2, 3, 5, 8, 13, 21};
        generator.j(numbers.length);
        assertEquals(expectedResult(numbers), actualResult());
    }

    private String expectedResult(long[] numbers) {
        StringBuilder sb = new StringBuilder();
        for (long n : numbers) {
            sb.append(n).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private String actualResult() {
        return buffer.toString();
    }
}
