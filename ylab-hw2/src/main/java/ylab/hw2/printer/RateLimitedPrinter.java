package ylab.hw2.printer;

public class RateLimitedPrinter {

    private final int interval;
    private long nextAllowedTimeToPrint;

    public RateLimitedPrinter(int interval) {
        this.interval = interval;
    }

    public void print(String message) {
        final long now = System.currentTimeMillis();
        if (nextAllowedTimeToPrint < now) {
            System.out.println(message);
            nextAllowedTimeToPrint = now + interval;
        }
    }
}
