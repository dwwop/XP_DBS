package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.OrderByClause;
import core.parsing.util.KeywordConsumer;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class OrderByFactoryTest {

    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
                EndOfFileError.class,
                () -> new OrderByFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "column_name", "SHOULD_NOT_BE_CONSUMED"
        ));

        OrderByClause clause = new OrderByFactory().fromTokens(tokens);
        assertEquals(new OrderByClause(Map.of("column_name", KeywordConsumer.Keyword.ASC)), clause);
        assertEquals("SHOULD_NOT_BE_CONSUMED", tokens.poll());
    }

    @Test
    public void fromTokensSpecifiedOrderOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "column_name", "DESC", "SHOULD_NOT_BE_CONSUMED"
        ));

        OrderByClause clause = new OrderByFactory().fromTokens(tokens);
        assertEquals(new OrderByClause(Map.of("column_name", KeywordConsumer.Keyword.DESC)), clause);
        assertEquals("SHOULD_NOT_BE_CONSUMED", tokens.poll());
    }


    @Test
    public void fromTokensMultipleColumnsWithoutOrderOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", ",", "col2", ",", "col3", "SHOULD_NOT_BE_CONSUMED"
        ));

        OrderByClause clause = new OrderByFactory().fromTokens(tokens);
        assertEquals(new OrderByClause(Map.of(
                        "col1", KeywordConsumer.Keyword.ASC,
                        "col2", KeywordConsumer.Keyword.ASC,
                        "col3", KeywordConsumer.Keyword.ASC)),
                clause);
        assertEquals("SHOULD_NOT_BE_CONSUMED", tokens.poll());
    }

    @Test
    public void fromTokensMultipleColumnsWithOrderOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", "ASC", ",", "col2", ",", "col3", "DESC", "SHOULD_NOT_BE_CONSUMED"
        ));

        OrderByClause clause = new OrderByFactory().fromTokens(tokens);
        assertEquals(new OrderByClause(Map.of(
                        "col1", KeywordConsumer.Keyword.ASC,
                        "col2", KeywordConsumer.Keyword.ASC,
                        "col3", KeywordConsumer.Keyword.DESC)),
                clause);
        assertEquals("SHOULD_NOT_BE_CONSUMED", tokens.poll());
    }

    @Test
    public void fromTokensEmptyAfterVal() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", ","
        ));
        assertThrows(EndOfFileError.class, () -> new OrderByFactory().fromTokens(tokens));
    }

    @Test
    public void fromTokensEmptyAfterOrder() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", "DESC", ","
        ));
        assertThrows(EndOfFileError.class, () -> new OrderByFactory().fromTokens(tokens));
    }


    @Test
    public void fromTokensEmptyAfterMultipleVal() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", "DESC", ",", "col2", ","
        ));
        assertThrows(EndOfFileError.class, () -> new OrderByFactory().fromTokens(tokens));
    }

    @Test
    public void fromTokensEmptyAfterMultipleOrder() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", "DESC", ",", "col2", "ASC", ","
        ));
        assertThrows(EndOfFileError.class, () -> new OrderByFactory().fromTokens(tokens));
    }

    @Test
    public void fromTokensMissingComma() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", "col2"
        ));
        new OrderByFactory().fromTokens(tokens);
        assertEquals("col2", tokens.poll());
    }

    @Test
    public void fromTokensStartingComma() {
        Queue<String> tokens = new LinkedList<>(List.of(
                ","
        ));
        assertThrows(TokenError.class, () -> new OrderByFactory().fromTokens(tokens));
    }

    @Test
    public void fromTokensMultipleComma() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", ",", ",", "col2"
        ));
        assertThrows(TokenError.class, () -> new OrderByFactory().fromTokens(tokens));
    }
}