package ylab.hw2.stats;

import java.util.Scanner;

public class StatsAccumulatorDemo {

    public static void main(String[] args) {
        StatsAccumulator accumulator = new StatsAccumulatorImpl();
        try (Scanner scanner = new Scanner(System.in)) {
            printState(accumulator);
            boolean processing = true;
            while (processing) {
                System.out.print("Введите целое число или exit: ");
                String input = scanner.nextLine();
                if ("exit".equals(input)) {
                    processing = false;
                } else {
                    processInput(accumulator, input);
                }
            }
        }
    }

    private static void processInput(StatsAccumulator accumulator, String input) {
        try {
            int value = Integer.parseInt(input);
            accumulator.add(value);
            printState(accumulator);
        } catch (NumberFormatException exception) {
            System.out.println("Введено некорректное число. Попробуйте еще раз.");
        }
    }

    private static void printState(StatsAccumulator accumulator) {
        if (accumulator.getCount() == 0) {
            System.out.println("Аккумулятор пуст.");
        } else {
            System.out.printf("Аккумулятор {количество=%d, минимум=%d, максимум=%d, среднее=%f}.%n",
                    accumulator.getCount(),
                    accumulator.getMin(),
                    accumulator.getMax(),
                    accumulator.getAvg());
        }
    }
}
