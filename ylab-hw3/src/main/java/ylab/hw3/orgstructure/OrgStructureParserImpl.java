package ylab.hw3.orgstructure;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OrgStructureParserImpl implements OrgStructureParser {
    private static final int DEFAULT_NUM_LINES_TO_SKIP = 1;
    private static final String FIELD_SEPARATOR = ";";
    private static final int NUM_FIELDS = 4;

    private final int numberOfLinesToSkip;

    public OrgStructureParserImpl() {
        this(DEFAULT_NUM_LINES_TO_SKIP);
    }

    public OrgStructureParserImpl(int numberOfLinesToSkip) {
        this.numberOfLinesToSkip = numberOfLinesToSkip;
    }

    @Override
    public Employee parseStructure(File csvFile) throws IOException {
        Map<Long,Employee> nonAssociated = readFile(csvFile);
        return buildStructure(nonAssociated);
    }

    private Employee buildStructure(Map<Long,Employee> employees) {
        Employee bossIdHolder = new Employee();
        employees.forEach((employeeId, employee) -> {
            if (employee.getBossId() == null) {
                if (bossIdHolder.getId() != null) {
                    throw new OrgStructureIllegalDataException("Файл содержит более одного сотрудника без boss_id");
                }
                bossIdHolder.setId(employee.getId());
            } else {
                Employee superior = employees.get(employee.getBossId());
                if (superior == null) {
                    throw new OrgStructureIllegalDataException(
                            "В файле отсутствует начальник сотрудника, boss_id=" + employee.getBossId());
                }
                employee.setBoss(superior);
                superior.getSubordinates().add(employee);
            }
        });
        if (bossIdHolder.getId() == null) {
            throw new OrgStructureIllegalDataException(
                    "В файле отсутствует босс, то есть сотрудник у которого boss_id не задан");
        }
        return employees.get(bossIdHolder.getId());
    }

    private Map<Long,Employee> readFile(File csvFile) throws IOException {
        Map<Long,Employee> map = new HashMap<>();
        try (InputStream inputStream = new FileInputStream(csvFile);
             Scanner scanner = new Scanner(inputStream)) {
            int numLines = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                numLines++;
                if (numLines > numberOfLinesToSkip) {
                    Employee emp = parseEmployee(line);
                    if (map.containsKey(emp.getId())) {
                        throw new OrgStructureIllegalDataException(
                                "Файл содержит неуникальные id сотрудников: " + emp.getId());
                    }
                    map.put(emp.getId(), emp);
                }
            }
        }
        return map;
    }

    Employee parseEmployee(String line) {
        final String[] tokens = line.split(FIELD_SEPARATOR);
        if (tokens.length != NUM_FIELDS) {
            throw new OrgStructureParsingException("Строка не содержит необходимого количества полей: " + line);
        }
        String idStr = tokens[0];
        if (idStr.isBlank()) {
            throw new OrgStructureParsingException("Строка не содержит обязательного поля id");
        }
        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException ex) {
            throw new OrgStructureParsingException("Некорректный формат поля id: " + idStr, ex);
        }
        String bossIdStr = tokens[1];
        Long bossId;
        if (bossIdStr.isBlank()) {
            bossId = null;
        } else {
            try {
                bossId = Long.parseLong(bossIdStr);
            } catch (NumberFormatException ex) {
                throw new OrgStructureParsingException("Некорректный формат поля boss_id: " + bossIdStr, ex);
            }
        }
        return new Employee(id, bossId, tokens[2], tokens[3]);
    }
}
