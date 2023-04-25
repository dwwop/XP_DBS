package core.parsing.util;

import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ComparatorConsumerTest {
    @Test
    public void EmptyTokens() {
        Queue<String> tokens = new LinkedList<>(List.of());
        assertThrows(EndOfFileError.class, () -> ComparatorConsumer.consumeComparatorOrFail(tokens));
    }

    @Test
    public void fromTokensMultipleComparators() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("=", "SHOULD_NOT_CONSUME"));
        ComparatorConsumer.consumeComparatorOrFail(tokens);
        assertEquals("SHOULD_NOT_CONSUME", tokens.poll());
    }

    @Test
    public void fromTokensMissingComparator() {
        Queue<String> tokens = new LinkedList<>(List.of("SHOULD_NOT_CONSUME"));
        assertThrows(TokenError.class, () -> ComparatorConsumer.consumeComparatorOrFail(tokens));
    }

}