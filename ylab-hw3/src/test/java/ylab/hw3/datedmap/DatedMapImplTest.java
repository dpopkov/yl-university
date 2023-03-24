package ylab.hw3.datedmap;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DatedMapImplTest {

    private static final String KEY_1 = "key1";
    private static final String KEY_2 = "key2";
    private static final String VALUE_1 = "value1";
    private static final String VALUE_2 = "value2";
    private static final long ALLOWED_DIFFERENCE_MILLISECONDS = 1L;

    private final DatedMap datedMap = new DatedMapImpl();

    @Test
    void whenPutKeyValue_thenContainsKeyValueAndInsertionDate() {
        datedMap.put(KEY_1, VALUE_1);
        Date expectedDate = new Date();
        assertTrue(datedMap.containsKey(KEY_1));
        assertEquals(VALUE_1, datedMap.get(KEY_1));
        Date insertionDate = datedMap.getKeyLastInsertionDate(KEY_1);
        assertDatesEquals(expectedDate, insertionDate);
    }

    @Test
    void whenPutNewValue_thenUpdatesValueAndInsertionDate() {
        datedMap.put(KEY_1, VALUE_1);
        Date expectedDate1 = new Date();
        Date insertionDate1 = datedMap.getKeyLastInsertionDate(KEY_1);
        assertDatesEquals(expectedDate1, insertionDate1);

        pause();
        datedMap.put(KEY_1, VALUE_2);
        Date expectedDate2 = new Date();
        Date insertionDate2 = datedMap.getKeyLastInsertionDate(KEY_1);
        assertDatesEquals(expectedDate2, insertionDate2);
        assertNotEquals(insertionDate1, insertionDate2);
    }

    @Test
    void whenPutOldValue_thenNotChangingInsertionDate() {
        datedMap.put(KEY_1, VALUE_1);
        Date expectedDate = new Date();
        Date insertionDate1 = datedMap.getKeyLastInsertionDate(KEY_1);
        assertDatesEquals(expectedDate, insertionDate1);

        datedMap.put(KEY_1, VALUE_1);
        Date insertionDate2 = datedMap.getKeyLastInsertionDate(KEY_1);
        assertEquals(expectedDate, insertionDate2);
        assertDatesEquals(insertionDate1, insertionDate2);
    }

    @Test
    void whenRemoveKey_thenDoesNotContainValueAndDate() {
        datedMap.put(KEY_1, VALUE_1);
        assertTrue(datedMap.containsKey(KEY_1));
        assertNotNull(datedMap.get(KEY_1));
        assertNotNull(datedMap.getKeyLastInsertionDate(KEY_1));

        datedMap.remove(KEY_1);
        assertFalse(datedMap.containsKey(KEY_1));
        assertNull(datedMap.get(KEY_1));
        assertNull(datedMap.getKeyLastInsertionDate(KEY_1));
    }

    @Test
    void testKeySet() {
        final Set<String> keySet = datedMap.keySet();
        datedMap.put(KEY_1, VALUE_1);
        assertTrue(keySet.contains(KEY_1));

        datedMap.put(KEY_2, VALUE_2);
        assertTrue(keySet.contains(KEY_1));
        assertTrue(keySet.contains(KEY_2));

        keySet.remove(KEY_1);
        assertFalse(keySet.contains(KEY_1));
        assertFalse(datedMap.containsKey(KEY_1));
        assertTrue(keySet.contains(KEY_2));
        assertTrue(datedMap.containsKey(KEY_2));

        keySet.clear();
        assertFalse(datedMap.containsKey(KEY_2));
    }

    private void assertDatesEquals(Date expected, Date actual) {
        long diffMilliseconds = Math.abs(actual.getTime() - expected.getTime());
        assertTrue(diffMilliseconds < ALLOWED_DIFFERENCE_MILLISECONDS,
                "Моменты времени не совпадают: разница превышает допустимое количество миллисекунд");
    }

    private void pause() {
        try {
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
