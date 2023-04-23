package core.parsing.tree.statements.factories;

import core.db.types.Literal;
import core.parsing.tree.statements.UpdateStatement;
import exceptions.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static org.junit.Assert.*;

public class UpdateFactoryTest {

    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
            SyntaxError.class,
            () -> new UpdateFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
            "table1", "SET", "col1", "=", "\"val1\",", "col2=", "0", "where", "a", "=", "1"
        ));

        UpdateStatement statement = new UpdateFactory().fromTokens(tokens);
        Map<String, Literal> values = statement.getSetClause().getColumnValues();

        assertEquals("table1", statement.getTableName());
        assertEquals(2, values.size());
        assertEquals("\"val1\"", values.get("col1").toString());
        assertEquals("0", values.get("col2").toString());
    }

    @Test
    public void fromTokensMissingWhere() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
            "table1", "SET", "col1", "=", "\"val1\",", "col2=", "0"
        ));

        UpdateStatement statement = new UpdateFactory().fromTokens(tokens);
        Map<String, Literal> values = statement.getSetClause().getColumnValues();

        assertEquals("table1", statement.getTableName());
        assertEquals(2, values.size());
        assertEquals("\"val1\"", values.get("col1").toString());
        assertEquals("0", values.get("col2").toString());
    }

    @Test
    public void fromTokensMissingSetClause() {
        Queue<String> tokens = new LinkedList<>(List.of(
            "table1", "SET", "where", "a", "=", "1"
        ));

        assertThrows(
            SyntaxError.class,
            () -> new UpdateFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensMissingKeyword() {
        Queue<String> tokens = new LinkedList<>(List.of(
            "table1", "col1", "=", "\"val1\",", "col2=", "0", "where", "a", "=", "1"
        ));

        assertThrows(
            SyntaxError.class,
            () -> new UpdateFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensMissingTableName() {
        Queue<String> tokens = new LinkedList<>(List.of(
            "SET", "col1", "=", "\"val1\",", "col2=", "0", "where", "a", "=", "1"
        ));

        assertThrows(
            SyntaxError.class,
            () -> new UpdateFactory().fromTokens(tokens)
        );

    }

    @Test
    public void fromTokensMultipleConsecutiveSpaces() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
            "table1", "", "", "SET", "", "col1", "=", "\"val1\",", "col2=", "0", "", "where", "a", "=", "1"
        ));

        UpdateStatement statement = new UpdateFactory().fromTokens(tokens);
        Map<String, Literal> values = statement.getSetClause().getColumnValues();

        assertEquals("table1", statement.getTableName());
        assertEquals(2, values.size());
        assertEquals("\"val1\"", values.get("col1").toString());
        assertEquals("0", values.get("col2").toString());
    }
}