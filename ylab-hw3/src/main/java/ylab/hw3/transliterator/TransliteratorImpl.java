package ylab.hw3.transliterator;

import java.util.HashMap;
import java.util.Map;

public class TransliteratorImpl implements Transliterator {

    private static final String CONTIGUOUS_CAPITAL_LETTERS = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final char NON_CONTIGUOUS_CAPITAL_LETTER = 'Ё';
    private static final char FIRST_LETTER = CONTIGUOUS_CAPITAL_LETTERS.charAt(0);
    private static final char LAST_LETTER = CONTIGUOUS_CAPITAL_LETTERS.charAt(CONTIGUOUS_CAPITAL_LETTERS.length() - 1);

    private static final Map<Character, String> DEFAULT_MAPPING = new HashMap<>();
    static {
        DEFAULT_MAPPING.put('А', "A");
        DEFAULT_MAPPING.put('Б', "B");
        DEFAULT_MAPPING.put('В', "V");
        DEFAULT_MAPPING.put('Г', "G");
        DEFAULT_MAPPING.put('Д', "D");
        DEFAULT_MAPPING.put('Е', "E");
        DEFAULT_MAPPING.put('Ё', "E");
        DEFAULT_MAPPING.put('Ж', "ZH");
        DEFAULT_MAPPING.put('З', "Z");
        DEFAULT_MAPPING.put('И', "I");
        DEFAULT_MAPPING.put('Й', "I");
        DEFAULT_MAPPING.put('К', "K");
        DEFAULT_MAPPING.put('Л', "L");
        DEFAULT_MAPPING.put('М', "M");
        DEFAULT_MAPPING.put('Н', "N");
        DEFAULT_MAPPING.put('О', "O");
        DEFAULT_MAPPING.put('П', "P");
        DEFAULT_MAPPING.put('Р', "R");
        DEFAULT_MAPPING.put('С', "S");
        DEFAULT_MAPPING.put('Т', "T");
        DEFAULT_MAPPING.put('У', "U");
        DEFAULT_MAPPING.put('Ф', "F");
        DEFAULT_MAPPING.put('Х', "KH");
        DEFAULT_MAPPING.put('Ц', "TS");
        DEFAULT_MAPPING.put('Ч', "CH");
        DEFAULT_MAPPING.put('Ш', "SH");
        DEFAULT_MAPPING.put('Щ', "SHCH");
        DEFAULT_MAPPING.put('Ъ', "IE");
        DEFAULT_MAPPING.put('Ы', "Y");
        DEFAULT_MAPPING.put('Ь', "");
        DEFAULT_MAPPING.put('Э', "E");
        DEFAULT_MAPPING.put('Ю', "IU");
        DEFAULT_MAPPING.put('Я', "IA");
    }

    private final Map<Character, String> mapping;

    public TransliteratorImpl() {
        this(DEFAULT_MAPPING);
    }

    public TransliteratorImpl(Map<Character, String> mapping) {
        this.mapping = mapping;
    }

    @Override
    public String transliterate(String source) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char currentCharacter = source.charAt(i);
            if (isRussianCapitalLetter(currentCharacter)) {
                builder.append(mapping.get(currentCharacter));
            } else {
                builder.append(currentCharacter);
            }
        }
        return builder.toString();
    }

    private boolean isRussianCapitalLetter(char ch) {
        return FIRST_LETTER <= ch && ch <= LAST_LETTER || ch == NON_CONTIGUOUS_CAPITAL_LETTER;
    }
}
