package ylab.hw3.filesort;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SorterTestDemo {

    public static void main(String[] args) throws IOException {
        int count = 300_000;
        File dataFile;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.printf("Вы уверены что хотите сгенерировать файл содержащий %d значений? ", count);
            String answer = scanner.nextLine();
            if (!("Y".equalsIgnoreCase(answer)) && !("Д".equalsIgnoreCase(answer))) {
                System.out.print("Введите другой размер: ");
                count = scanner.nextInt();
            }
            dataFile = new Generator().generate("data.txt", count);
            System.out.println(new Validator(dataFile).isSorted());
            File sortedFile = new Sorter().sortFile(dataFile);
            System.out.println(new Validator(sortedFile).isSorted());
        }
    }
}
