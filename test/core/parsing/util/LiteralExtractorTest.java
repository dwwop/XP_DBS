package core.parsing.util;

import core.db.types.IntegerLiteral;
import core.db.types.Literal;
import core.db.types.StringLiteral;
import exceptions.syntax.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

public class LiteralExtractorTest {

    @Test
    public void pollLiteralOrFailNoTokens() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
                SyntaxError.class,
                () -> LiteralExtractor.pollLiteralOrFail(tokens)
        );
    }

    @Test
    public void pollLiteralOrFailString() throws SyntaxError {
        String expected = "\"example string\"";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        Literal literal = LiteralExtractor.pollLiteralOrFail(tokens);

        assertTrue(literal instanceof StringLiteral);
        assertEquals(expected, literal.toString());
        assertEquals(2, tokens.size());
    }

    @Test
    public void pollLiteralOrFailStringNoEndingQuote() {
        String expected = "\"example string";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        assertThrows(
                SyntaxError.class,
                () -> LiteralExtractor.pollLiteralOrFail(tokens)
        );
        assertEquals(3, tokens.size());
    }

    @Test
    public void pollLiteralOrFailStringNoQuotes() {
        String expected = "example string";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        assertThrows(
                SyntaxError.class,
                () -> LiteralExtractor.pollLiteralOrFail(tokens)
        );
        assertEquals(3, tokens.size());
    }

    @Test
    public void pollLiteralOrFailStringEmpty() throws SyntaxError {
        String expected = "\"\"";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        Literal literal = LiteralExtractor.pollLiteralOrFail(tokens);

        assertTrue(literal instanceof StringLiteral);
        assertEquals(expected, literal.toString());
        assertEquals(2, tokens.size());
    }

    @Test
    public void pollLiteralOrFailStringMultipleConsecutiveSpaces() throws SyntaxError {
        String expected = "\"example    string \"";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        Literal literal = LiteralExtractor.pollLiteralOrFail(tokens);

        assertTrue(literal instanceof StringLiteral);
        assertEquals(expected, literal.toString());
        assertEquals(2, tokens.size());
    }

    @Test
    public void pollLiteralOrFailStringNumber() throws SyntaxError {
        String expected = "\"42\"";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        Literal literal = LiteralExtractor.pollLiteralOrFail(tokens);

        assertTrue(literal instanceof StringLiteral);
        assertEquals(expected, literal.toString());
        assertEquals(2, tokens.size());
    }

    @Test
    public void pollLiteralOrFailInteger() throws SyntaxError {
        String expected = "3276";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        Literal literal = LiteralExtractor.pollLiteralOrFail(tokens);

        assertTrue(literal instanceof IntegerLiteral);
        assertEquals(expected, literal.toString());
        assertEquals(2, tokens.size());
    }

    @Test
    public void pollLiteralOrFailIntegerZero() throws SyntaxError {
        String expected = "0";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        Literal literal = LiteralExtractor.pollLiteralOrFail(tokens);

        assertTrue(literal instanceof IntegerLiteral);
        assertEquals(expected, literal.toString());
        assertEquals(2, tokens.size());
    }

    @Test
    public void pollLiteralOrFailIntegerNegative() throws SyntaxError {
        String expected = "-432";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        Literal literal = LiteralExtractor.pollLiteralOrFail(tokens);

        assertTrue(literal instanceof IntegerLiteral);
        assertEquals(expected, literal.toString());
        assertEquals(2, tokens.size());
    }

    @Test
    public void pollLiteralOrFailIntegerOutOfRange() throws SyntaxError {
        String expected = (((long) Integer.MAX_VALUE) + 1) + "";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        assertThrows(
                SyntaxError.class,
                () -> LiteralExtractor.pollLiteralOrFail(tokens)
        );
        assertEquals(3, tokens.size());
    }

    @Test
    public void pollLiteralOrFailIntegerNegativeOutOfRange() {
        String expected = (((long) Integer.MIN_VALUE) - 1) + "";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        assertThrows(
                SyntaxError.class,
                () -> LiteralExtractor.pollLiteralOrFail(tokens)
        );
        assertEquals(3, tokens.size());
    }

    @Test
    public void pollLiteralOrFailIntegerInvalid() {
        String expected = "123 + 876";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "something", "else"));

        assertThrows(
                SyntaxError.class,
                () -> LiteralExtractor.pollLiteralOrFail(tokens)
        );
        assertEquals(3, tokens.size());
    }

    @Test
    public void pollLiteralTypeOrFailNoTokens() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
                SyntaxError.class,
                () -> LiteralExtractor.pollLiteralTypeOrFail(tokens)
        );
    }

    @Test
    public void pollLiteralTypeOrFailString() throws SyntaxError {
        String expected = "string";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "constraint"));

        Literal.Type type = LiteralExtractor.pollLiteralTypeOrFail(tokens);

        assertEquals(Literal.Type.String, type);
        assertEquals(1, tokens.size());
    }

    @Test
    public void pollLiteralTypeOrFailStringShort() throws SyntaxError {
        String expected = "STR";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "constraint"));

        Literal.Type type = LiteralExtractor.pollLiteralTypeOrFail(tokens);

        assertEquals(Literal.Type.String, type);
        assertEquals(1, tokens.size());
    }

    @Test
    public void pollLiteralTypeOrFailInteger() throws SyntaxError {
        String expected = "INTEGER";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "constraint"));

        Literal.Type type = LiteralExtractor.pollLiteralTypeOrFail(tokens);

        assertEquals(Literal.Type.Integer, type);
        assertEquals(1, tokens.size());
    }

    @Test
    public void pollLiteralTypeOrFailIntegerShort() throws SyntaxError {
        String expected = "int";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "constraint"));

        Literal.Type type = LiteralExtractor.pollLiteralTypeOrFail(tokens);

        assertEquals(Literal.Type.Integer, type);
        assertEquals(1, tokens.size());
    }

    @Test
    public void pollLiteralTypeOrFailMixedCase() throws SyntaxError {
        String expected = "Str";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "constraint"));

        Literal.Type type = LiteralExtractor.pollLiteralTypeOrFail(tokens);

        assertEquals(Literal.Type.String, type);
        assertEquals(1, tokens.size());
    }

    @Test
    public void pollLiteralTypeOrFailInvalid() {
        String expected = "none";
        Queue<String> tokens = new LinkedList<>(List.of(expected, "constraint"));

        assertThrows(
                SyntaxError.class,
                () -> LiteralExtractor.pollLiteralTypeOrFail(tokens)
        );
        assertEquals(2, tokens.size());
    }
}