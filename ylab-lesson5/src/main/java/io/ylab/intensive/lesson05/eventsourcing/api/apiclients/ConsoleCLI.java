package io.ylab.intensive.lesson05.eventsourcing.api.apiclients;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.api.PersonApi;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Command Line UI для интерактивного взаимодействия с PersonApi.
 */
public class ConsoleCLI {

    private final PersonApi api;
    private final InputStream inputStream;
    private final PrintStream out;

    public ConsoleCLI(PersonApi api, InputStream inputStream, PrintStream out) {
        this.api = api;
        this.inputStream = inputStream;
        this.out = out;
    }

    public void start() {
        try (Scanner scanner = new Scanner(inputStream)) {
            boolean isRunning = true;
            while (isRunning) {
                String cmd = prompt(scanner);
                switch (cmd.toLowerCase()) {
                    case "exit":
                        isRunning = false;
                        break;
                    case "save":
                        save(scanner);
                        break;
                    case "delete":
                        delete(scanner);
                        break;
                    case "findbyid":
                        find(scanner);
                        break;
                    case "findall":
                        findAll();
                        break;
                    default:
                        out.println("Некорректная команда.");
                        break;
                }
            }
        }
    }

    private String prompt(Scanner scanner) {
        out.println("Введите команду (save, delete, findById, findAll, exit): ");
        return scanner.nextLine();
    }

    private void save(Scanner scanner) {
        out.print("Введите (в одной строке разделяя пробелами) id, firstName, lastName, middleName: ");
        long saveId = scanner.nextLong();
        String firstName = scanner.next();
        String lastName = scanner.next();
        String middleName = scanner.nextLine();
        api.savePerson(saveId, firstName, lastName, middleName);
    }

    private void delete(Scanner scanner) {
        out.print("Введите id для удаления: ");
        long deleteId = scanner.nextLong();
        scanner.nextLine();
        api.deletePerson(deleteId);
    }

    private void find(Scanner scanner) {
        out.print("Введите id для поиска: ");
        long findId = scanner.nextLong();
        scanner.nextLine();
        Person person = api.findPerson(findId);
        out.println("Найден: " + person);
    }

    private void findAll() {
        List<Person> persons = api.findAll();
        out.println("Найдено " + persons.size());
        for (Person p : persons) {
            System.out.println(p);
        }
    }
}
