package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.api.apiclients.ConsoleCLI;
import io.ylab.intensive.lesson05.eventsourcing.api.apiclients.SimpleDemo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApiApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        PersonApi personApi = applicationContext.getBean(PersonApi.class);

        String mode = args.length > 0 ? args[0] : "interactive";
        if ("interactive".equalsIgnoreCase(mode)) {
            ConsoleCLI cli = new ConsoleCLI(personApi, System.in, System.out);
            cli.start();
        } else {
            SimpleDemo demo = new SimpleDemo(personApi, System.out);
            demo.start();
        }
    }
}
