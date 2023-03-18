package ylab.hw2.snils;

import java.util.regex.Pattern;

public class SnilsValidatorImpl implements SnilsValidator {

    private static final int NUM_OF_SNILS_DIGITS = 11;
    private static final int NUM_OF_CONTROL_NUMBER_DIGITS = 2;
    private static final int START_OF_CONTROL_PART = NUM_OF_SNILS_DIGITS - NUM_OF_CONTROL_NUMBER_DIGITS;
    private static final Pattern pattern = Pattern.compile("\\d{" + NUM_OF_SNILS_DIGITS + "}");

    @Override
    public boolean validate(String snils) {
        if (snils == null
                || snils.isBlank()
                || snils.length() != NUM_OF_SNILS_DIGITS
                || !pattern.matcher(snils).matches()) {
            return false;
        }
        int controlSum = calculateControlSum(snils);
        int calculatedControlNumber = calculateControlNumber(controlSum);
        int actualControlNumber = Integer.parseInt(snils.substring(START_OF_CONTROL_PART));
        return calculatedControlNumber == actualControlNumber;
    }

    private int calculateControlNumber(int controlSum) {
        if (controlSum < 100) {
            return controlSum;
        } else if (controlSum == 100) {
            return 0;
        } else {
            int remainder = controlSum % 101;
            if (remainder == 100) {
                return 0;
            } else {
                return remainder;
            }
        }
    }

    private int calculateControlSum(String snils) {
        int sum = 0;
        for (int i = 0; i < START_OF_CONTROL_PART; i++) {
            int digit = snils.charAt(i) - '0';
            sum += digit * (START_OF_CONTROL_PART - i);
        }
        return sum;
    }
}
