package ylab.hw3.datedmap;

import java.util.Date;
import java.util.Set;

/**
 * Структура данных аналогичная Map, содержащая дополнительную информацию о времени добавления каждого ключа.
 * Время добавления хранится только для тех ключей, которые присутствуют в Map.
 */
public interface DatedMap {

    void put(String key, String value);
    String get(String key);
    boolean containsKey(String key);
    void remove(String key);
    Set<String> keySet();
    Date getKeyLastInsertionDate(String key);
}
