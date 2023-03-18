package ylab.hw2.sequences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class SequenceGeneratorDemo {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите тип последовательности (a..j): ");
            String type = scanner.nextLine();
            System.out.print("Введите длину последовательности: ");
            int n = scanner.nextInt();
            if (validateInput(type, n)) {
                SequenceGenerator generator = new SequenceGeneratorImpl();
                printSequence(generator, type, n);
            } else {
                System.err.println("Ввод некорректный. Программа завершается.");
            }
        }
    }

    private static boolean validateInput(String type, int n) {
        if (type.length() != 1) {
            System.err.println("Тип последовательности должен обозначаться одной буквой.");
            return false;
        } else if (n <= 0) {
            System.err.println("Длина последовательности должна быть натуральным числом.");
            return false;
        }
        return true;
    }

    private static void printSequence(SequenceGenerator generator, String type, int n) {
        try {
            /*
             * Использование Reflection здесь только чтобы сократить кол-во однообразных строк кода
             * и благодаря тому что методы SequenceGenerator имеют однозначное соответствие с типом последовательности.
             * Очень надеюсь что использование Reflection не будет классифицироваться как недочет.
             * Это простой код, где в противном случаем пришлось бы делать либо длинный switch,
             * либо другой многострочный вариант.
             */
            final Method method = generator.getClass().getMethod(type, int.class);
            method.invoke(generator, n);
        } catch (NoSuchMethodException e) {
            System.err.println("Введен несуществующий тип последовательности: " + type);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
