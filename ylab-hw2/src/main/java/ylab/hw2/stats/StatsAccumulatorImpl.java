package ylab.hw2.stats;

public class StatsAccumulatorImpl implements StatsAccumulator {

    private int count;
    private int total;
    private int minimalValue;
    private int maximalValue;

    public StatsAccumulatorImpl() {
        minimalValue = Integer.MAX_VALUE;
        maximalValue = Integer.MIN_VALUE;
    }

    @Override
    public void add(int value) {
        count++;
        total += value;
        minimalValue = Math.min(minimalValue, value);
        maximalValue = Math.max(maximalValue, value);
    }

    @Override
    public int getMin() {
        return minimalValue;
    }

    @Override
    public int getMax() {
        return maximalValue;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Double getAvg() {
        if (count == 0) {
            return null;
        }
        return (double) total / count;
    }
}
