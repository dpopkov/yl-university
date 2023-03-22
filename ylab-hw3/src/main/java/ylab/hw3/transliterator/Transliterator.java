package ylab.hw3.transliterator;

public interface Transliterator {

    /**
     * Выполняет транслитерацию входной строки заменяя каждый символ кириллицы в верхнем регистре
     * на соответствующую группу символов латиницы.
     * @param source входная строка
     * @return строка с замененными символами
     */
    String transliterate(String source);
}
