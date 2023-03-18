package ylab.hw2.stats;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatsAccumulatorImplTest {

    private static final double DELTA = 1e-14;

    @Test
    void whenAddingValues_thenChangesStateAccordingly() {
        StatsAccumulator accumulator = new StatsAccumulatorImpl();
        assertEquals(0, accumulator.getCount());
        assertNull(accumulator.getAvg());

        accumulator.add(1);
        accumulator.add(2);
        assertEquals(2, accumulator.getCount());
        assertEquals(1.5, accumulator.getAvg(), DELTA);
        assertEquals(1, accumulator.getMin());
        assertEquals(2, accumulator.getMax());

        accumulator.add(0);
        assertEquals(3, accumulator.getCount());
        assertEquals(1.0, accumulator.getAvg(), DELTA);
        assertEquals(0, accumulator.getMin());
        assertEquals(2, accumulator.getMax());

        accumulator.add(3);
        accumulator.add(8);
        assertEquals(5, accumulator.getCount());
        assertEquals(2.8, accumulator.getAvg(), DELTA);
        assertEquals(0, accumulator.getMin());
        assertEquals(8, accumulator.getMax());
    }
}
