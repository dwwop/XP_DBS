package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.SelectClause;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SelectClauseFactoryTest {
    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
                EndOfFileError.class,
                () -> new SelectClauseFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensAllOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "*", "SHOULD_NOT_BE_CONSUMED"
        ));

        SelectClause clause = new SelectClauseFactory().fromTokens(tokens);
        SelectClause expectedClause = new SelectClause(true);
        assertEquals(expectedClause.isAllColumns(), clause.isAllColumns());
        assertEquals(expectedClause.getColumnNames(), clause.getColumnNames());
        assertEquals("SHOULD_NOT_BE_CONSUMED", tokens.poll());
    }

    @Test
    public void fromTokensOneColumnOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", "SHOULD_NOT_BE_CONSUMED"
        ));

        SelectClause clause = new SelectClauseFactory().fromTokens(tokens);
        SelectClause expectedClause = new SelectClause(List.of("col1"));
        assertEquals(expectedClause.isAllColumns(), clause.isAllColumns());
        assertEquals(expectedClause.getColumnNames(), clause.getColumnNames());
        assertEquals("SHOULD_NOT_BE_CONSUMED", tokens.poll());
    }

    @Test
    public void fromTokensMultipleColumnOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", ",", "col2", ",", "col3", "SHOULD_NOT_BE_CONSUMED"
        ));

        SelectClause clause = new SelectClauseFactory().fromTokens(tokens);
        SelectClause expectedClause = new SelectClause(List.of("col1", "col2", "col3"));
        assertEquals(expectedClause.isAllColumns(), clause.isAllColumns());
        assertEquals(expectedClause.getColumnNames(), clause.getColumnNames());
        assertEquals("SHOULD_NOT_BE_CONSUMED", tokens.poll());
    }

    @Test
    public void fromTokensAllAfterColName() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", ",", "*", ","
        ));

        assertThrows(TokenError.class, () -> new SelectClauseFactory().fromTokens(tokens));
    }

    @Test
    public void fromTokensStartingComma() {
        Queue<String> tokens = new LinkedList<>(List.of(
                ","
        ));

        assertThrows(TokenError.class, () -> new SelectClauseFactory().fromTokens(tokens));
    }

    @Test
    public void fromTokensEndingComma() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", ","
        ));

        assertThrows(EndOfFileError.class, () -> new SelectClauseFactory().fromTokens(tokens));
    }


    @Test
    public void fromTokensMultipleCommas() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col1", ",", ",", "col2"
        ));

        assertThrows(TokenError.class, () -> new SelectClauseFactory().fromTokens(tokens));
    }
}