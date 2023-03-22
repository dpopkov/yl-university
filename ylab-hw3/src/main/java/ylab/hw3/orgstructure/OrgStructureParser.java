package ylab.hw3.orgstructure;

import java.io.File;
import java.io.IOException;

public interface OrgStructureParser {

    /**
     * Считывает данные из файла и возвращает ссылку на Босса - сотрудника,
     * атрибут boss_id которого не задан. Обязательным условием является то что такой
     * сотрудник в файле ровно один.
     * @param csvFile файл содержащий данные в CSV формате
     * @return сотрудник являющийся боссом всех остальных сотрудников
     * @throws IOException в случае ошибок чтения файла данных
     */
    Employee parseStructure(File csvFile) throws IOException;
}
