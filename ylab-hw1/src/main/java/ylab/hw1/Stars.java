package ylab.hw1;

import java.util.Scanner;

public class Stars {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ввод:");
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            String template = scanner.next();
            if (inputIsValid(n) && inputIsValid(m) && inputIsValid(template)) {
                String output = buildOutput(n, m, template.charAt(0));
                System.out.println("Вывод:");
                System.out.println(output);
            } else {
                System.out.println("Программа завершена. Повторите попытку с другими данными.");
            }
        }
    }

    private static String buildOutput(int numRows, int numColumns, char symbol) {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                if (col > 0) {
                    builder.append(" ");
                }
                builder.append(symbol);
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    private static boolean inputIsValid(int value) {
        if (value <= 0) {
            System.err.printf("Ввод должен быть натуральным числом. Введено некорректное число: %d.%n", value);
            return false;
        }
        return true;
    }

    private static boolean inputIsValid(String symbol) {
        if (symbol.length() != 1) {
            System.err.printf("Ввод символа должен содержать 1 букву. Введена слишком длинная строка: %s%n", symbol);
            return false;
        }
        return true;
    }
}
