package ylab.hw1;

import java.util.Random;
import java.util.Scanner;

public class Guess {

    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 99;
    private static final int MAX_ATTEMPTS = 10;

    public static void main(String[] args) {
        int number = createRandomNumber(MIN_NUMBER, MAX_NUMBER);
        System.out.printf("Я загадал число от %d до %d. У тебя %d попыток угадать.%n",
                MIN_NUMBER, MAX_NUMBER, MAX_ATTEMPTS);
        try (Scanner scanner = new Scanner(System.in)) {
            int count = 0;
            boolean didntGuess = true;
            while (didntGuess && count < MAX_ATTEMPTS) {
                final int guess = scanner.nextInt();
                count++;
                if (guess == number) {
                    System.out.println("Ты угадал с " + count + " попытки");
                    didntGuess = false;
                } else if (number < guess) {
                    System.out.println("Мое число меньше! Осталось " + (MAX_ATTEMPTS - count) + " попыток");
                } else {
                    System.out.println("Мое число больше! Осталось " + (MAX_ATTEMPTS - count) + " попыток");
                }
            }
            if (didntGuess) {
                System.out.println("Ты не угадал.");
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static int createRandomNumber(int from, int toUpperBoundInclusive) {
        return from + new Random().nextInt(toUpperBoundInclusive - from + 1);
    }
}
