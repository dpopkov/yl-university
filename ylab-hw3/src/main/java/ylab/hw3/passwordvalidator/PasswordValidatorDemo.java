package ylab.hw3.passwordvalidator;

import java.util.Scanner;

public class PasswordValidatorDemo {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите логин: ");
            String login = scanner.nextLine();
            System.out.print("Введите пароль: ");
            String password = scanner.nextLine();
            System.out.print("Введите пароль повторно: ");
            String passwordConfirmation = scanner.nextLine();
            boolean result = PasswordValidator.validate(login, password, passwordConfirmation);
            System.out.println("Результат валидации: " + result);
        }
    }
}
