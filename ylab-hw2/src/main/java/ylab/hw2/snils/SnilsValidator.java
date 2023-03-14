package ylab.hw2.snils;

public interface SnilsValidator {

    /**
     * Проверяет, что в строке содержится валидный номер СНИЛС.
     * @param snils снилс для проверки
     * @return результат проверки
     */
    boolean validate(String snils);
}
