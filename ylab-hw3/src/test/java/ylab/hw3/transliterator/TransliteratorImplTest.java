package ylab.hw3.transliterator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransliteratorImplTest {

    private final Transliterator transliterator = new TransliteratorImpl();

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/ylab/hw3/transliterator/transliterate-test-data.csv",
            useHeadersInDisplayName = true)
    void testTransliterate(String input, String expected) {
        String actual = transliterator.transliterate(input);
        assertEquals(expected, actual);
    }

    @Test
    void whenInputContainsNonRussianOrLowerCaseLetters_thenKeepThem() {
        String input = "Hello! ПРИВЕТ! это Java.";
        String expected = "Hello! PRIVET! это Java.";
        String actual = transliterator.transliterate(input);
        assertEquals(expected, actual);
    }
}
