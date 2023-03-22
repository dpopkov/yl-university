package ylab.hw3.passwordvalidator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    private PrintStream savedOut;
    private ByteArrayOutputStream buffer;

    @BeforeEach
    void setUp() {
        savedOut = System.out;
        buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
    }

    @Test
    void testValidate() {
        boolean result = PasswordValidator.validate("Login_12", "Pass_123", "Pass_123");
        assertTrue(result);
    }

    @Test
    void testValidate_whenAllEmpty_thenTrue() {
        boolean result = PasswordValidator.validate("", "", "");
        assertTrue(result);
    }

    @Test
    void testValidate_whenIncorrectLoginCharacters_thenFalse() {
        boolean result = PasswordValidator.validate("%", "", "");
        assertFalse(result);
        assertEquals("Логин содержит недопустимые символы\n", buffer.toString());
    }

    @Test
    void testValidate_whenLoginIsTooLong_thenFalse() {
        boolean result = PasswordValidator.validate("12345678901234567890a", "", "");
        assertFalse(result);
        assertEquals("Логин слишком длинный\n", buffer.toString());
    }

    @Test
    void testValidate_whenIncorrectPasswordCharacters_thenFalse() {
        boolean result = PasswordValidator.validate("login", "&", "");
        assertFalse(result);
        assertEquals("Пароль содержит недопустимые символы\n", buffer.toString());
    }

    @Test
    void testValidate_whenPasswordIsTooLong_thenFalse() {
        boolean result = PasswordValidator.validate("login", "12345678901234567890a", "");
        assertFalse(result);
        assertEquals("Пароль слишком длинный\n", buffer.toString());
    }

    @Test
    void testValidate_whenPasswordNotEqualsConfirmation_thenFalse() {
        boolean result = PasswordValidator.validate("login", "12345678", "abc");
        assertFalse(result);
        assertEquals("Пароль и подтверждение не совпадают\n", buffer.toString());
    }

    @AfterEach
    void tearDown() {
        System.setOut(savedOut);
    }
}
