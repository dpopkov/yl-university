package ylab.hw1;

public class MultTable {

    private static final int MIN_FACTOR = 1;
    private static final int MAX_FACTOR = 9;

    public static void main(String[] args) {
        for (int i = MIN_FACTOR; i <= MAX_FACTOR; i++) {
            for (int j = MIN_FACTOR; j <= MAX_FACTOR; j++) {
                System.out.printf("%d x %d = %d%n", i, j, (i * j));
            }
        }
    }
}
