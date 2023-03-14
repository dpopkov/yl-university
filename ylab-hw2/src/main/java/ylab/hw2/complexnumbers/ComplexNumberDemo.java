package ylab.hw2.complexnumbers;

import java.util.Scanner;

public class ComplexNumberDemo {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean processing = true;
            while (processing) {
                System.out.print("Введите тип операции (add, subtract, multiply, modulus, exit): ");
                String type = scanner.nextLine();
                if ("exit".equals(type)) {
                    processing = false;
                } else {
                    processOperation(scanner, type);
                }
            }
        }
    }

    private static void processOperation(Scanner scanner, String type) {
        ComplexNumber n1 = readComplexNumber(scanner);
        if ("modulus".equals(type)) {
            double result = n1.modulus();
            System.out.printf("Модуль числа %s равен %f%n", n1, result);
        } else {
            ComplexNumber n2 = readComplexNumber(scanner);
            ComplexNumber result = null;
            switch (type) {
                case "add":
                    result = n1.add(n2);
                    break;
                case "subtract":
                    result = n1.subtract(n2);
                    break;
                case "multiply":
                    result = n1.multiply(n2);
                    break;
                default:
                    System.err.println("Недопустимый тип операции: " + type);
                    break;
            }
            if (result != null) {
                System.out.println("Результат: " + result);
            }
        }
    }

    private static ComplexNumber readComplexNumber(Scanner scanner) {
        System.out.print("Введите вещественную и мнимую части числа: ");
        double real = scanner.nextDouble();
        double imaginary = scanner.nextDouble();
        scanner.nextLine();
        return new ComplexNumber(real, imaginary);
    }
}
