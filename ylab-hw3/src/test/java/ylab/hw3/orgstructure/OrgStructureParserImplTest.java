package ylab.hw3.orgstructure;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrgStructureParserImplTest {

    private final OrgStructureParserImpl parser = new OrgStructureParserImpl();

    @Test
    void testParseStructure() throws URISyntaxException, IOException {
        URL url = OrgStructureParserImplTest.class.getResource("org-structure-test-data.csv");
        Employee boss = parser.parseStructure(new File(url.toURI()));
        assertNotNull(boss);
        assertThat(boss).isEqualTo(new Employee(1L, null, "Иван Иванович", "Генеральный директор"));

        List<Employee> bossSubordinates = boss.getSubordinates();
        assertThat(bossSubordinates).hasSize(3)
                .contains(new Employee(2L, 1L, "Крокодилова Людмила Петровна", "Главный бухгалтер"),
                        new Employee(4L, 1L, "Сидоров Василий Васильевич", "Исполнительный директор"),
                        new Employee(5L, 1L, "Зайцев Валерий Петрович", "Директор по ИТ"));

        Optional<Employee> krokodilova = bossSubordinates.stream().filter(emp -> emp.getId() == 2L).findFirst();
        List<Employee> krokodilovaSubordinates = krokodilova.orElseThrow().getSubordinates();
        assertThat(krokodilovaSubordinates).hasSize(1)
                .contains(new Employee(3L, 2L, "Галочка", "Бухгалтер"));

        Optional<Employee> zaitsev = bossSubordinates.stream().filter(emp -> emp.getId() == 5L).findFirst();
        List<Employee> zaitsevSubordinates = zaitsev.orElseThrow().getSubordinates();
        assertThat(zaitsevSubordinates).hasSize(1)
                .contains(new Employee(6L, 5L, "Петя", "Программист"));
    }

    @Test
    void testParseStructure_whenNotHavingSuperiorEmployee_thenThrowException() {
        URL url = OrgStructureParserImplTest.class.getResource("org-structure-test-data-no-superior.csv");
        assertThrows(OrgStructureIllegalDataException.class,
                () -> parser.parseStructure(new File(url.toURI())));
    }

    @Test
    void testParseEmployee() {
        String input = "6;5;Петя;Программист";
        Employee employee = parser.parseEmployee(input);
        assertEquals(6L, employee.getId());
        assertEquals(5L, employee.getBossId());
        assertEquals("Петя", employee.getName());
        assertEquals("Программист", employee.getPosition());
    }

    @Test
    void testParseEmployee_whenNotEnoughFields_thenThrowException() {
        String input = "6;5;Петя";
        assertThrows(OrgStructureParsingException.class, () -> parser.parseEmployee(input));
    }

    @Test
    void testParseEmployee_whenNotContainingId_thenThrowException() {
        String input = " ;5;Петя;Программист";
        assertThrows(OrgStructureParsingException.class, () -> parser.parseEmployee(input));
    }

    @Test
    void testParseEmployee_whenNotCorrectId_thenThrowException() {
        String input = "abc;5;Петя;Программист";
        assertThrows(OrgStructureParsingException.class, () -> parser.parseEmployee(input));
    }
}
