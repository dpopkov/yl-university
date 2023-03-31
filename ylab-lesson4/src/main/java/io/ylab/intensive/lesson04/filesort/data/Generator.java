package io.ylab.intensive.lesson04.filesort.data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Generator {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите имя файла для сохранения: ");
            String filename = scanner.nextLine();
            System.out.print("Введите количество чисел для генерации: ");
            int count = scanner.nextInt();
            File result = generate(filename, count);
            System.out.println("В файл " + result + " записано " + count + " чисел.");
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
    }

    public static File generate(String name, int count) throws IOException {
        Random random = new Random();
        File file = new File(name);
        try (PrintWriter writer = new PrintWriter(file)) {
            for (int i = 0; i < count; i++) {
                writer.println(random.nextLong());
            }
            writer.flush();
        }
        return file;
    }
}
