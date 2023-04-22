package core.parsing.util;

import exceptions.syntax.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.*;

public class KeywordConsumerTest {

    @Test
    public void isKeywordEmptyToken() {
        String token = "";

        assertFalse(KeywordConsumer.isKeyword(KeywordConsumer.Keyword.INTO, token));
    }

    @Test
    public void isKeywordTrue() {
        String token = "INTO";

        assertTrue(KeywordConsumer.isKeyword(KeywordConsumer.Keyword.INTO, token));
    }

    @Test
    public void isKeywordMixedCase() {
        String token = "Where";

        assertTrue(KeywordConsumer.isKeyword(KeywordConsumer.Keyword.WHERE, token));
    }

    @Test
    public void isKeywordOtherKeyword() {
        String token = "ORDER";

        assertFalse(KeywordConsumer.isKeyword(KeywordConsumer.Keyword.VALUES, token));
    }

    @Test
    public void consumeKeywordNoTokens() {
        Queue<String> tokens = new LinkedList<>();

        assertFalse(KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.BY, tokens));
    }

    @Test
    public void consumeKeywordTrue() {
        Queue<String> tokens = new LinkedList<>(List.of("By", "continues"));

        assertTrue(KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.BY, tokens));
        assertEquals(1, tokens.size());
    }

    @Test
    public void consumeKeywordEmptyTokensBefore() {
        Queue<String> tokens = new LinkedList<>(List.of("", "", "from", "continues"));

        assertTrue(KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.FROM, tokens));
        assertEquals(1, tokens.size());
    }

    @Test
    public void consumeKeywordInvalid() {
        Queue<String> tokens = new LinkedList<>(List.of("continues"));

        assertFalse(KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.FROM, tokens));
        assertEquals(1, tokens.size());
    }

    @Test
    public void consumeKeywordOtherKeyword() {
        Queue<String> tokens = new LinkedList<>(List.of("table"));

        assertFalse(KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.SET, tokens));
        assertEquals(1, tokens.size());
    }

    @Test
    public void consumeKeywordOrFailNoTokens() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
            SyntaxError.class,
            () -> KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.BY, tokens)
        );
    }

    @Test
    public void consumeKeywordOrFailTrue() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("By", "continues"));

        KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.BY, tokens);

        assertEquals(1, tokens.size());
    }

    @Test
    public void consumeKeywordOrFailEmptyTokensBefore() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("", "", "from", "continues"));

        KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.FROM, tokens);

        assertEquals(1, tokens.size());
    }

    @Test
    public void consumeKeywordOrFailInvalid() {
        Queue<String> tokens = new LinkedList<>(List.of("continues"));

        assertThrows(
            SyntaxError.class,
            () -> KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.LIMIT, tokens)
        );

        assertEquals(1, tokens.size());
    }

    @Test
    public void consumeKeywordOrFailOtherKeyword() {
        Queue<String> tokens = new LinkedList<>(List.of("offset", "continues"));

        assertThrows(
            SyntaxError.class,
            () -> KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.ORDER, tokens)
        );

        assertEquals(2, tokens.size());
    }

    @Test
    public void consumeKeywordOrFailMultipleNoTokens() {
        Queue<String> tokens = new LinkedList<>();
        Set<KeywordConsumer.Keyword> keywords = Set.of(KeywordConsumer.Keyword.TABLE, KeywordConsumer.Keyword.FROM);

        assertThrows(
            SyntaxError.class,
            () -> KeywordConsumer.consumeKeywordOrFail(keywords, tokens)
        );
    }

    @Test
    public void consumeKeywordOrFailMultipleTrue() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("table", "continues"));
        Set<KeywordConsumer.Keyword> keywords = Set.of(KeywordConsumer.Keyword.TABLE, KeywordConsumer.Keyword.FROM);

        KeywordConsumer.Keyword keyword = KeywordConsumer.consumeKeywordOrFail(keywords, tokens);

        assertEquals(KeywordConsumer.Keyword.TABLE, keyword);
    }

    @Test
    public void consumeKeywordOrFailMultipleEmptyTokensBefore() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("", "", "from", "continues"));
        Set<KeywordConsumer.Keyword> keywords = Set.of(KeywordConsumer.Keyword.OFFSET, KeywordConsumer.Keyword.FROM);

        KeywordConsumer.Keyword keyword = KeywordConsumer.consumeKeywordOrFail(keywords, tokens);

        assertEquals(KeywordConsumer.Keyword.FROM, keyword);
    }

    @Test
    public void consumeKeywordOrFailMultipleInvalid() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("unknown"));
        Set<KeywordConsumer.Keyword> keywords = Set.of(KeywordConsumer.Keyword.FROM, KeywordConsumer.Keyword.WHERE);

        assertThrows(
            SyntaxError.class,
            () -> KeywordConsumer.consumeKeywordOrFail(keywords, tokens)
        );

        assertEquals(1, tokens.size());
    }

    @Test
    public void consumeKeywordOrFailMultipleOtherKeyword() {
        Queue<String> tokens = new LinkedList<>(List.of("offset", "continues"));
        Set<KeywordConsumer.Keyword> keywords = Set.of(KeywordConsumer.Keyword.INTO, KeywordConsumer.Keyword.WHERE);

        assertThrows(
            SyntaxError.class,
            () -> KeywordConsumer.consumeKeywordOrFail(keywords, tokens)
        );

        assertEquals(2, tokens.size());
    }
}