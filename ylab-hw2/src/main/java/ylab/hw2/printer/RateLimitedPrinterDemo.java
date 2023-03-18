package ylab.hw2.printer;

import java.util.Scanner;

public class RateLimitedPrinterDemo {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите количество попыток печати (например 100000000): ");
            int numAttempts = scanner.nextInt();
            System.out.print("Введите интервал в миллисекундах (например 1000): ");
            int interval = scanner.nextInt();
            RateLimitedPrinter printer = new RateLimitedPrinter(interval);
            for (int i = 0; i < numAttempts; i++) {
                printer.print(String.valueOf(i));
            }
        }
    }
}
