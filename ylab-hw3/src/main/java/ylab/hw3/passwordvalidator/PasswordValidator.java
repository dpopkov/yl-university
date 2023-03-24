package ylab.hw3.passwordvalidator;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final int LOGIN_MAXIMUM_LENGTH = 20;
    private static final int PASSWORD_MAXIMUM_LENGTH = 20;
    private static final Pattern pattern = Pattern.compile("[a-zA-Z0-9_]*");

    public static boolean validate(String login, String password, String confirmPassword) {
        try {
            if (!pattern.matcher(login).matches()) {
                throw new WrongLoginException("Логин содержит недопустимые символы");
            } else if (login.length() > LOGIN_MAXIMUM_LENGTH) {
                throw new WrongLoginException("Логин слишком длинный");
            } else if (!pattern.matcher(password).matches()) {
                throw new WrongPasswordException("Пароль содержит недопустимые символы");
            } else if (password.length() > PASSWORD_MAXIMUM_LENGTH) {
                throw new WrongPasswordException("Пароль слишком длинный");
            } else if (!password.equals(confirmPassword)) {
                throw new WrongPasswordException("Пароль и подтверждение не совпадают");
            }
        } catch (WrongLoginException | WrongPasswordException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
}
