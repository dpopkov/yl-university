package ylab.hw3.orgstructure;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class OrgStructureParserDemo {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите путь к файлу CSV: ");
            String path = scanner.nextLine();
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("Указанный файл не существует. Попробуйте еще раз.");
            } else {
                OrgStructureParser parser = new OrgStructureParserImpl();
                Employee boss = parser.parseStructure(file);
                printStructure(boss, "");
            }
        } catch (IOException ioEx) {
            System.err.println("Ошибка чтения файла: " + ioEx.getMessage());
        } catch (OrgStructureParsingException | OrgStructureIllegalDataException structureEx) {
            System.err.println("Ошибка в структуре файла: " + structureEx.getMessage());
        }
    }

    private static void printStructure(Employee employee, String indent) {
        System.out.println(indent + employee);
        List<Employee> subordinates = employee.getSubordinates();
        if (subordinates != null && subordinates.size() > 0) {
            indent += "\t";
            for (Employee emp : subordinates) {
                printStructure(emp, indent);
            }
        }
    }
}
