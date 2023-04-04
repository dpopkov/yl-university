package io.ylab.intensive.lesson05.messagefilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Производит проверку строк на наличие недопустимых слов.
 * При их наличии производит замену срединных символов на *.
 */
@Component
public class MessageFilter {
    private static final Logger log = LoggerFactory.getLogger(MessageFilter.class);

    public static final String DELIMITING_SYMBOLS = ".,;?!";
    private static final Pattern SPLITTING_PATTERN = Pattern.compile("\\s+|[" + DELIMITING_SYMBOLS + "]");
    private static final String REPLACEMENT_SYMBOL = "*";

    private final WordCheckingDbClient dbClient;

    public MessageFilter(WordCheckingDbClient dbClient) {
        this.dbClient = dbClient;
    }

    /**
     * Проверяет строку на наличие недопустимых слов и при необходимости заменяет их
     * на измененные версии. Если таких слов не обнаружено, то возвращает строку без
     * изменений.
     * @param text проверяемый текст
     * @return текст с произведенной заменой, либо без изменений
     */
    public String filter(String text) {
        log.debug("Проверка текста: {}", text);
        final StringBuilder buffer = new StringBuilder(text);
        final String[] words = splitToWords(text);
        final Set<String> checkedInText = new HashSet<>();
        try {
            this.dbClient.startConnection();
            for (String word : words) {
                if (!checkedInText.contains(word)) {
                    if (this.dbClient.containsWord(word.toLowerCase())) {
                        doAllReplacements(buffer, word);
                    }
                    checkedInText.add(word);
                }
            }
            this.dbClient.stopConnection();
        } catch (SQLException ex) {
            log.error("Не могу проверить текст из-за ошибки в БД", ex);
            return null;
        }
        return buffer.toString();
    }

    private void doAllReplacements(StringBuilder buffer, String word) {
        final int wordLength = word.length();
        String cleanedWord = word.charAt(0) + REPLACEMENT_SYMBOL.repeat(wordLength - 2) + word.charAt(wordLength - 1);
        int start = findIndexOfWord(buffer, word, 0);
        while (start != -1) {
            buffer.replace(start, start + wordLength, cleanedWord);
            start = findIndexOfWord(buffer, word, start + wordLength);
        }
    }

    private int findIndexOfWord(StringBuilder buffer, String word, int from) {
        final int wordLength = word.length();
        int start = buffer.indexOf(word, from);
        while (start != -1) {
            int end = start + wordLength;
            if (isLeftBoundary(buffer, start) && isRightBoundary(buffer, end - 1)) {
                return start;
            }
            start = buffer.indexOf(word, end);
        }
        return start;
    }

    private boolean isLeftBoundary(StringBuilder builder, int pos) {
        if (pos == 0) {
            return true;
        }
        return isDelimitingSymbol(builder.charAt(pos - 1));
    }

    private boolean isRightBoundary(StringBuilder builder, int pos) {
        if (pos == builder.length() - 1) {
            return true;
        }
        return isDelimitingSymbol(builder.charAt(pos + 1));
    }

    private boolean isDelimitingSymbol(char ch) {
         if (Character.isWhitespace(ch)) {
            return true;
        }
        for (int i = 0; i < DELIMITING_SYMBOLS.length(); i++) {
            if (DELIMITING_SYMBOLS.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    private String[] splitToWords(String text) {
        return SPLITTING_PATTERN.split(text);
    }
}
