package core.parsing.tree.clauses.factories;

import core.db.types.Literal;
import exceptions.syntax.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

public class ValuesFactoryTest {

    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
                SyntaxError.class,
                () -> new ValuesFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensInvalidList() {
        Queue<String> tokens = new LinkedList<>(List.of("invalid"));

        assertThrows(
                SyntaxError.class,
                () -> new ValuesFactory().fromTokens(tokens)
        );
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void fromTokensEmptyTuple() {
        Queue<String> tokens = new LinkedList<>(List.of("(", ")"));

        assertThrows(
                SyntaxError.class,
                () -> new ValuesFactory().fromTokens(tokens)
        );
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void fromTokensOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("(1,", "2", ",", "3", ")", ",", "(4,", "5,", "6)", ",", "(\"7\",", "\"8\",", "\"9\")"));
        List<List<Literal>> values = new ValuesFactory().fromTokens(tokens).getValues();

        assertEquals(3, values.size());
        assertEquals("1", values.get(0).get(0).toString());
        assertEquals("2", values.get(0).get(1).toString());
        assertEquals("3", values.get(0).get(2).toString());
        assertEquals("4", values.get(1).get(0).toString());
        assertEquals("5", values.get(1).get(1).toString());
        assertEquals("6", values.get(1).get(2).toString());
        assertEquals("\"7\"", values.get(2).get(0).toString());
        assertEquals("\"8\"", values.get(2).get(1).toString());
        assertEquals("\"9\"", values.get(2).get(2).toString());
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void fromTokensVariousLengths() {
        Queue<String> tokens = new LinkedList<>(List.of("(1,", "2", ",", "3", ")", "(4,", "5)", "(\"7\",", "\"8\",", "\"9\")"));

        assertThrows(
                SyntaxError.class,
                () -> new ValuesFactory().fromTokens(tokens)
        );
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void fromTokensInvalidLiterals() {
        Queue<String> tokens = new LinkedList<>(List.of("(not,", "a,", "valid,", "literal)"));

        assertThrows(
                SyntaxError.class,
                () -> new ValuesFactory().fromTokens(tokens)
        );
        assertTrue(tokens.isEmpty());
    }
}