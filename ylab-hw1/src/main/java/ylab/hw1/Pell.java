package ylab.hw1;

import java.util.Scanner;

public class Pell {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ввод:");
            int n = scanner.nextInt();
            validateInput(n);
            int result = pellNumber(n);
            System.out.println("Вывод:");
            System.out.println(result);
        }
    }

    private static int pellNumber(int n) {
        if (n == 0 || n == 1) {
            return n;
        }
        int prev = 0;
        int current = 1;
        for (int i = 2; i <= n; i++) {
            int next = 2 * current + prev;
            prev = current;
            current = next;
        }
        return current;
    }

    private static void validateInput(int value) {
        if (value < 0 || 30 < value) {
            System.err.printf("Число должно быть от 0 до 30 включительно. Введено некорректное число: %d.%n", value);
            System.exit(0);
        }
    }
}
