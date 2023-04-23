package core.parsing.tree.statements.factories;

import core.parsing.tree.statements.DeleteStatement;
import exceptions.syntax.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DeleteFactoryTest {

    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
                SyntaxError.class,
                () -> new DeleteFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "FROM", "table1", "where", "a", "=", "1"
        ));

        DeleteStatement statement = new DeleteFactory().fromTokens(tokens);

        assertEquals("table1", statement.getTableName());
    }

    @Test
    public void fromTokensMissingWhere() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "FROM", "table1"
        ));

        DeleteStatement statement = new DeleteFactory().fromTokens(tokens);

        assertEquals("table1", statement.getTableName());
    }

    @Test
    public void fromTokensMissingKeyword() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "table1", "where", "a", "=", "1"
        ));

        assertThrows(
                SyntaxError.class,
                () -> new DeleteFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensMissingTableName() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "FROM", "where", "a", "=", "1"
        ));

        assertThrows(
                SyntaxError.class,
                () -> new DeleteFactory().fromTokens(tokens)
        );

    }

    @Test
    public void fromTokensMultipleConsecutiveSpaces() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "", "FROM", "", "table1", "", "", "where", "a", "=", "1"
        ));

        DeleteStatement statement = new DeleteFactory().fromTokens(tokens);

        assertEquals("table1", statement.getTableName());
    }
}