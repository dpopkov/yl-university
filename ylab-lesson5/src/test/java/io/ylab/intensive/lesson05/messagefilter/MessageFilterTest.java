package io.ylab.intensive.lesson05.messagefilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageFilterTest {

    @Mock
    WordCheckingDbClient dbClient;
    MessageFilter messageFilter;

    @BeforeEach
    void init() {
        messageFilter = new MessageFilter(dbClient);
    }

    @Test
    void testFilter_whenFindsWord_thenReplacesAllInstances() throws SQLException {
        when(dbClient.containsWord("one")).thenReturn(true);
        String text = "one two\nONE three.one,two,ONE;three?one!ONE\tOne";
        String expected = "o*e two\nO*E three.o*e,two,O*E;three?o*e!O*E\tO*e";
        String actual = messageFilter.filter(text);
        assertEquals(expected, actual);
    }

    @Test
    void testFilter_whenFindsWordWhichIsPartOfAnother_thenReplacesOnlyCompleteMatch() throws SQLException {
        when(dbClient.containsWord("one")).thenReturn(true);
        String text = "one two onelove";
        String expected = "o*e two onelove";
        String actual = messageFilter.filter(text);
        assertEquals(expected, actual);
    }

    @Test
    void testFilter_whenFindsWordWhichIsPartOfAnother_thenAllowsReplacingComplexWord() throws SQLException {
        when(dbClient.containsWord(anyString())).thenReturn(true, false, true);
        String text = "one two onelove";
        String expected = "o*e two o*****e";
        String actual = messageFilter.filter(text);
        assertEquals(expected, actual);
    }
}
