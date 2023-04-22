package core.parsing.tree.clauses.factories;

import core.db.types.Literal;
import exceptions.syntax.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static org.junit.Assert.*;

public class SetFactoryTest {

    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
            SyntaxError.class,
            () -> new SetFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensInvalidAssignment() {
        Queue<String> tokens = new LinkedList<>(List.of("invalid"));

        assertThrows(
            SyntaxError.class,
            () -> new SetFactory().fromTokens(tokens)
        );
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void fromTokensMissingComma() {
        Queue<String> tokens = new LinkedList<>(List.of("col1=1", "col2", "=", "2", "where"));

        assertThrows(
            SyntaxError.class,
            () -> new SetFactory().fromTokens(tokens)
        );
        assertEquals(1, tokens.size());
    }

    @Test
    public void fromTokensInvalidLiteral() {
        Queue<String> tokens = new LinkedList<>(List.of("col1=a"));

        assertThrows(
            SyntaxError.class,
            () -> new SetFactory().fromTokens(tokens)
        );
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void fromTokensOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("col1=1", ",", "col2", "=", "\"some  where to test\"", "where"));
        Map<String, Literal> values = new SetFactory().fromTokens(tokens).getColumnValues();

        assertEquals(2, values.size());
        assertEquals("1", values.get("col1").toString());
        assertEquals("\"some  where to test\"", values.get("col2").toString());
        assertEquals(1, tokens.size());
    }
}