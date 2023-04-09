package io.ylab.intensive.lesson05.eventsourcing.api.apiclients;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.api.PersonApi;

import java.io.PrintStream;
import java.util.List;

/**
 * Клиент PersonApi производящий простую последовательность действий
 * включающую сохранение Person, нахождение по Id, нахождение всех Person,
 * удаление Person по Id.
 */
public class SimpleDemo {

    private final PersonApi personApi;
    private final PrintStream out;

    public SimpleDemo(PersonApi personApi, PrintStream out) {
        this.personApi = personApi;
        this.out = out;
    }

    public void start() {
        personApi.savePerson(1L, "John", "Doe", "J");
        personApi.savePerson(2L, "Jane", "Doe", "");
        giveTimeForOperationExecution();
        printPersons(personApi.findAll());

        Person john = personApi.findPerson(1L);
        System.out.println("Found: " + john);

        personApi.deletePerson(2L);
        giveTimeForOperationExecution();
        printPersons(personApi.findAll());

        personApi.savePerson(1L, "John", "Modified", "");
        giveTimeForOperationExecution();
        printPersons(personApi.findAll());
    }

    private void printPersons(List<Person> list) {
        this.out.println("Found " + list.size() + " Persons");
        for (Person person : list) {
            this.out.println(person);
        }
    }

    private static void giveTimeForOperationExecution() {
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
