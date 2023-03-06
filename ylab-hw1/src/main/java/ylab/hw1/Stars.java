package ylab.hw1;

import java.util.Scanner;

public class Stars {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ввод:");
            int n = scanner.nextInt();
            validateInput(n);
            int m = scanner.nextInt();
            validateInput(m);
            String template = scanner.next();
            validateInput(template);
            String output = buildOutput(n, m, template.charAt(0));
            System.out.println("Вывод:");
            System.out.println(output);
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

    private static void validateInput(int value) {
        if (value <= 0) {
            System.err.printf("Ввод должен быть натуральным числом. Введено некорректное число: %d.%n", value);
            System.exit(0);
        }
    }

    private static void validateInput(String symbol) {
        if (symbol.length() != 1) {
            System.err.printf("Ввод должен содержать 1 символ. Введена некорректная строка: %s%n", symbol);
            System.exit(0);
        }
    }
}
