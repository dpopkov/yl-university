package ylab.hw3.datedmap;

import java.util.*;

/**
 * Основная имплементация интерфейса {@link DatedMap} в которой используется
 * только одна внутренняя структура данных - HashMap, а хранение дат осуществляется
 * с помощью дополнительного вложенного класса.
 * Данная реализация предпочтительней чем {@link DatedMapImplUsingTwoMaps},
 * так как последняя использует 2 мапы и выполняет лишние операции с ключом.
 */
public class DatedMapImpl implements DatedMap {
    private final Map<String, DatedString> innerMap = new HashMap<>();

    @Override
    public void put(String key, String value) {
        innerMap.put(key, new DatedString(value));
    }

    @Override
    public String get(String key) {
        DatedString found = innerMap.get(key);
        return found != null ? found.getValue() : null;
    }

    @Override
    public boolean containsKey(String key) {
        return innerMap.containsKey(key);
    }

    @Override
    public void remove(String key) {
        innerMap.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return innerMap.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        DatedString found = innerMap.get(key);
        return found != null ? found.getDate() : null;
    }

    private static class DatedString {
        private final Date date;
        private final String value;

        private DatedString(String value) {
            this.date = new Date();
            this.value = value;
        }

        public Date getDate() {
            return date;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DatedString that = (DatedString) o;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }
}
