package core.parsing.util;

import exceptions.syntax.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

public class RawQueryTokenizerTest {

    @Test
    public void tokenizeQueryEmpty() throws SyntaxError {
        Queue<String> tokens = RawQueryTokenizer.tokenizeQuery("");
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void tokenizeQuerySingleWord() throws SyntaxError {
        Queue<String> tokens = RawQueryTokenizer.tokenizeQuery("one");

        assertEquals("one", tokens.poll());
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void tokenizeQueryMutipleWords() throws SyntaxError {
        Queue<String> tokens = RawQueryTokenizer.tokenizeQuery("one t-w-o (t_h_r_e_e)");

        assertEquals("one", tokens.poll());
        assertEquals("t-w-o", tokens.poll());
        assertEquals("(", tokens.poll());
        assertEquals("t_h_r_e_e", tokens.poll());
        assertEquals(")", tokens.poll());
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void tokenizeQueryMutipleWordsMultipleConsecutiveSpaces() throws SyntaxError {
        Queue<String> tokens = RawQueryTokenizer.tokenizeQuery("one   t-w-o (t_h_r_e_e)");

        assertEquals("one", tokens.poll());
        assertEquals("t-w-o", tokens.poll());
        assertEquals("(", tokens.poll());
        assertEquals("t_h_r_e_e", tokens.poll());
        assertEquals(")", tokens.poll());
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void tokenizeTupleTrimmingOrFailEmpty() {
        assertThrows(
                SyntaxError.class,
                () -> RawQueryTokenizer.tokenizeTupleTrimmingOrFail(RawQueryTokenizer.TupleType.Schema, "")
        );
    }

    @Test
    public void tokenizeTupleTrimmingOrFailOK() throws SyntaxError {
        Queue<String> tokens = RawQueryTokenizer.tokenizeTupleTrimmingOrFail(RawQueryTokenizer.TupleType.ColumnsList, "(col1, col2, col3, col4)");

        assertEquals("col1", tokens.poll());
        assertEquals("col2", tokens.poll());
        assertEquals("col3", tokens.poll());
        assertEquals("col4", tokens.poll());
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void tokenizeTupleTrimmingOrFailInvalidFormat() {
        assertThrows(
                SyntaxError.class,
                () -> RawQueryTokenizer.tokenizeTupleTrimmingOrFail(RawQueryTokenizer.TupleType.ValueTuple, "col1, col2,  col3, col4 )")
        );
    }

    @Test
    public void tokenizeTupleTrimmingOrFailMultipleConsecutiveSpaces() throws SyntaxError {
        Queue<String> tokens = RawQueryTokenizer.tokenizeTupleTrimmingOrFail(RawQueryTokenizer.TupleType.ColumnsList, "(col1, col2,  col3, col4 )");

        assertEquals("col1", tokens.poll());
        assertEquals("col2", tokens.poll());
        assertEquals("col3", tokens.poll());
        assertEquals("col4", tokens.poll());
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void consumeEmptyTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        RawQueryTokenizer.consumeEmptyTokens(tokens);

        assertTrue(tokens.isEmpty());
    }

    @Test
    public void consumeEmptyTokens() {
        Queue<String> tokens = new LinkedList<>(List.of("", "", "token"));

        RawQueryTokenizer.consumeEmptyTokens(tokens);

        assertEquals("token", tokens.peek());
    }


    @Test
    public void specialCharactersQouted() throws SyntaxError {
        Queue<String> tokens = RawQueryTokenizer.tokenizeQuery("\"( ), = != < > <= >=\"  (), = != < > <= >=");

        assertEquals("\"( ), = != < > <= >=\"", tokens.poll());
        assertEquals("(", tokens.poll());
        assertEquals(")", tokens.poll());
        assertEquals(",", tokens.poll());
        assertEquals("=", tokens.poll());
        assertEquals("!=", tokens.poll());
        assertEquals("<", tokens.poll());
        assertEquals(">", tokens.poll());
        assertEquals("<=", tokens.poll());
        assertEquals(">=", tokens.poll());
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void unclosedStringQoute() {
        assertThrows(SyntaxError.class, () -> RawQueryTokenizer.tokenizeQuery("\"uiwfah "));
    }
}