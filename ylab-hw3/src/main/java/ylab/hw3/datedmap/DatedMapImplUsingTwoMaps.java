package ylab.hw3.datedmap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Имплементация интерфейса {@link DatedMap} в которой используется
 * две внутренней структуры данных - одна для хранения строк, а другая для хранения дат.
 * Данная реализация не поддерживает в полной мере ту же семантику метода <code>keySet</code>,
 * которая предложена в настоящей {@link Map} и где keySet позволяет удалять пары ключ-значения.
 * Более предпочтительна реализация {@link DatedMapImpl}, в которой используется
 * только одна внутренняя структура данных.
 */
public class DatedMapImplUsingTwoMaps implements DatedMap {

    private final Map<String, String> keyValueMap = new HashMap<>();
    private final Map<String, Date> keyDateMap = new HashMap<>();

    @Override
    public void put(String key, String value) {
        String previousValue = keyValueMap.put(key, value);
        if (previousValue == null || !previousValue.equals(value)) {
            keyDateMap.put(key, new Date());
        }
    }

    @Override
    public String get(String key) {
        return keyValueMap.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return keyValueMap.containsKey(key);
    }

    @Override
    public void remove(String key) {
        keyValueMap.remove(key);
        keyDateMap.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return keyValueMap.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        return keyDateMap.get(key);
    }
}
