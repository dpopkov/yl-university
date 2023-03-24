package ylab.hw3.transliterator;

import java.util.Scanner;

public class TransliteratorDemo {

    public static void main(String[] args) {
        Transliterator transliterator = new TransliteratorImpl();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите строку для транслитерации: ");
            String input = scanner.nextLine();
            String result = transliterator.transliterate(input);
            System.out.println(result);
        }
    }
}
