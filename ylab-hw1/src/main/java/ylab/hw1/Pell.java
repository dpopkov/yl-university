package ylab.hw1;

import java.math.BigInteger;
import java.util.Scanner;

public class Pell {

    private static final int MAXIMUM_N_FOR_32BIT_INTEGER = 25;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ввод:");
            int n = scanner.nextInt();
            if (inputIsValid(n)) {
                if (n <= MAXIMUM_N_FOR_32BIT_INTEGER) {
                    int result = pellNumber(n);
                    System.out.println("Вывод:");
                    System.out.println(result);
                } else {
                    BigInteger result = pellNumberBig(n);
                    System.out.println("Вывод:");
                    System.out.println(result);
                }
            } else {
                System.out.println("Программа завершена. Повторите попытку с другими данными.");
            }
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

    private static BigInteger pellNumberBig(int n) {
        if (n <= MAXIMUM_N_FOR_32BIT_INTEGER) {
            return BigInteger.valueOf(pellNumber(n));
        }
        BigInteger prev = BigInteger.ZERO;
        BigInteger current = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            BigInteger next = current.multiply(BigInteger.TWO).add(prev);
            prev = current;
            current = next;
        }
        return current;
    }

    private static boolean inputIsValid(int value) {
        if (value < 0 || 30 < value) {
            System.err.printf("Число должно быть от 0 до 30 включительно. Введено некорректное число: %d.%n", value);
            return false;
        }
        return true;
    }
}
