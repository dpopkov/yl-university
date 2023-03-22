package ylab.hw3.orgstructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Employee {

    private Long id;
    private Long bossId;
    private String name;
    private String position;
    private Employee boss;
    private final List<Employee> subordinates = new ArrayList<>();

    Employee() {
    }

    public Employee(Long id, Long bossId, String name, String position) {
        this.id = id;
        this.bossId = bossId;
        this.name = name;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBossId() {
        return bossId;
    }

    public void setBossId(Long bossId) {
        this.bossId = bossId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Employee getBoss() {
        return boss;
    }

    public void setBoss(Employee boss) {
        this.boss = boss;
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    /*
     * Методы equals() и hashCode() добавлены только для облегчения написания тестов (для AssertJ).
     * В основном коде они не используются.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id)
                && Objects.equals(bossId, employee.bossId)
                && Objects.equals(name, employee.name)
                && Objects.equals(position, employee.position);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (bossId != null ? bossId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", bossId=" + bossId +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
