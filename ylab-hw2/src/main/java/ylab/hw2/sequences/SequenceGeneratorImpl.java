package ylab.hw2.sequences;

import java.io.PrintStream;
import java.util.function.LongUnaryOperator;

/**
 * Реализация {@link SequenceGenerator} не содержащая проверок на переполнение.
 * Предполагается что для данной задачи не будут применяться
 * длины последовательностей приводящие к переполнению типа <code>long</code>.
 */
public class SequenceGeneratorImpl implements SequenceGenerator {

    private final PrintStream out;

    public SequenceGeneratorImpl() {
        this(System.out);
    }

    public SequenceGeneratorImpl(PrintStream out) {
        this.out = out;
    }

    @Override
    public void a(int n) {
        printSimpleSequence(n, 2, value -> value + 2);
    }

    @Override
    public void b(int n) {
        printSimpleSequence(n, 1, value -> value + 2);
    }

    @Override
    public void c(int n) {
        printFunctionalSequence(n, 1, value -> value + 1, value -> value * value);
    }

    @Override
    public void d(int n) {
        printFunctionalSequence(n, 1, value -> value + 1, value -> value * value * value);
    }

    @Override
    public void e(int n) {
        printSimpleSequence(n, 1, value -> -value);
    }

    @Override
    public void f(int n) {
        printFunctionalSequenceAlternatingSign(n, 1, v -> v + 1, v -> v);
    }

    @Override
    public void g(int n) {
        printFunctionalSequenceAlternatingSign(n, 1, v -> v + 1, v -> v * v);
    }

    @Override
    public void h(int n) {
        long number = 1;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                out.println(number);
                number++;
            } else {
                out.println(0);
            }
        }
    }

    @Override
    public void i(int n) {
        long number = 1;
        for (int i = 1; i <= n; i++) {
            number *= i;
            out.println(number);
        }
    }

    @Override
    public void j(int n) {
        long prev = 0;
        long current = 1;
        for (int i = 0; i < n; i++) {
            out.println(current);
            long next = prev + current;
            prev = current;
            current = next;
        }
    }

    private void printSimpleSequence(int n, long initial, LongUnaryOperator producerOfNext) {
        long value = initial;
        for (int i = 0; i < n; i++) {
            out.println(value);
            value = producerOfNext.applyAsLong(value);
        }
    }

    private void printFunctionalSequence(int n, long initial, LongUnaryOperator producerOfNext, LongUnaryOperator function) {
        long value = initial;
        for (int i = 0; i < n; i++) {
            out.println(function.applyAsLong(value));
            value = producerOfNext.applyAsLong(value);
        }
    }

    private void printFunctionalSequenceAlternatingSign(int n, long initial, LongUnaryOperator producerOfNext, LongUnaryOperator function) {
        long value = initial;
        long signFactor = 1;
        for (int i = 0; i < n; i++) {
            out.println(function.applyAsLong(value) * signFactor);
            value = producerOfNext.applyAsLong(value);
            signFactor = -signFactor;
        }
    }
}
