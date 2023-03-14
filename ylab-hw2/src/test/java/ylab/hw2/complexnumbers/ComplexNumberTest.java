package ylab.hw2.complexnumbers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplexNumberTest {

    private static final double DELTA = 1e-14;

    private static Stream<Arguments> additionData() {
        return Stream.of(
                Arguments.of(new ComplexNumber(1.0, 2.0), new ComplexNumber(3.0, 4.0),
                        new ComplexNumber(4.0, 6.0)),
                Arguments.of(new ComplexNumber(1.0, 2.0), new ComplexNumber(-3.0, -4.0),
                        new ComplexNumber(-2.0, -2.0)),
                Arguments.of(new ComplexNumber(1.0), new ComplexNumber(3.0, 4.0),
                        new ComplexNumber(4.0, 4.0)),
                Arguments.of(new ComplexNumber(1.0), new ComplexNumber(3.0),
                        new ComplexNumber(4.0))
        );
    }

    @ParameterizedTest
    @MethodSource("additionData")
    void testAdd(ComplexNumber original, ComplexNumber addendum, ComplexNumber expected) {
        ComplexNumber sum = original.add(addendum);
        assertComplexEquals(expected, sum);
    }

    private static Stream<Arguments> subtractionData() {
        return Stream.of(
                Arguments.of(new ComplexNumber(1.0, 2.0), new ComplexNumber(3.0, 4.0),
                        new ComplexNumber(-2.0, -2.0)),
                Arguments.of(new ComplexNumber(1.0, 2.0), new ComplexNumber(-3.0, -4.0),
                        new ComplexNumber(4.0, 6.0)),
                Arguments.of(new ComplexNumber(1.0), new ComplexNumber(3.0, 4.0),
                        new ComplexNumber(-2.0, -4.0)),
                Arguments.of(new ComplexNumber(1.0), new ComplexNumber(3.0),
                        new ComplexNumber(-2.0))
        );
    }

    @ParameterizedTest
    @MethodSource("subtractionData")
    void testSubtract(ComplexNumber original, ComplexNumber subtrahend, ComplexNumber expected) {
        ComplexNumber diff = original.subtract(subtrahend);
        assertComplexEquals(expected, diff);
    }

    private static Stream<Arguments> multiplicationData() {
        return Stream.of(
                Arguments.of(new ComplexNumber(1.0, 2.0), new ComplexNumber(3.0, 4.0),
                        new ComplexNumber(-5.0, 10.0)),
                Arguments.of(new ComplexNumber(1.0, 2.0), new ComplexNumber(-3.0, -4.0),
                        new ComplexNumber(5.0, -10)),
                Arguments.of(new ComplexNumber(1.0), new ComplexNumber(3.0, 4.0),
                        new ComplexNumber(3.0, 4.0)),
                Arguments.of(new ComplexNumber(1.0), new ComplexNumber(3.0),
                        new ComplexNumber(3.0))
        );
    }

    @ParameterizedTest
    @MethodSource("multiplicationData")
    void testMultiply(ComplexNumber original, ComplexNumber multiplier, ComplexNumber expected) {
        ComplexNumber product = original.multiply(multiplier);
        assertComplexEquals(expected, product);
    }

    private static Stream<Arguments> modulusData() {
        return Stream.of(
                Arguments.of(new ComplexNumber(1.0, 2.0), Math.sqrt(5.0)),
                Arguments.of(new ComplexNumber(3.0, 4.0), 5.0),
                Arguments.of(new ComplexNumber(-3.0, -4.0), 5.0),
                Arguments.of(new ComplexNumber(-1.0), 1.0)
        );
    }

    @ParameterizedTest
    @MethodSource("modulusData")
    void testModulus(ComplexNumber cn, double expected) {
        double actual = cn.modulus();
        assertEquals(expected, actual, DELTA);
    }

    private void assertComplexEquals(ComplexNumber a, ComplexNumber b) {
        assertEquals(a.getReal(), b.getReal(), DELTA);
        assertEquals(a.getImaginary(), b.getImaginary(), DELTA);
    }

    private static Stream<Arguments> toStringData() {
        return Stream.of(
                Arguments.of(new ComplexNumber(1.0, 2.0), "1.0 + 2.0i"),
                Arguments.of(new ComplexNumber(3.0, -4.0), "3.0 - 4.0i"),
                Arguments.of(new ComplexNumber(-3.0, -4.0), "-3.0 - 4.0i"),
                Arguments.of(new ComplexNumber(1.0), "1.0"),
                Arguments.of(new ComplexNumber(-1.0), "-1.0")
        );
    }

    @ParameterizedTest
    @MethodSource("toStringData")
    void testToString(ComplexNumber number, String expected) {
        assertEquals(expected, number.toString());
    }
}
