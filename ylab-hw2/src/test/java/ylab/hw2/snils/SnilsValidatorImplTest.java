package ylab.hw2.snils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnilsValidatorImplTest {

    private final SnilsValidator validator = new SnilsValidatorImpl();

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "123",
            "123456789012",
            "1a345678901"
    })
    void whenSnilsDoesNotLookLikeSnils_thenFalse(String snils) {
        assertFalse(validator.validate(snils));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "01468870570",
            "54537055502",
            "12602903634"
    })
    void whenSnilsIsNotValid_thenFalse(String snils) {
        assertFalse(validator.validate(snils));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "90114404441",
            "64537055502",
            "12602903624"
    })
    void whenSnilsIsValid_thenTrue(String snils) {
        assertTrue(validator.validate(snils));
    }
}
