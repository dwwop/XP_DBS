package core.parsing.tree.statements.factories;

import core.db.types.Literal;
import core.parsing.tree.statements.InsertStatement;
import core.parsing.tree.statements.UpdateStatement;
import exceptions.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static org.junit.Assert.*;

public class InsertFactoryTest {

    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
            SyntaxError.class,
            () -> new InsertFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
            "INTO", "table1", "(col1,", "col2", ")", "VALUES", "(\"val1\",1),", "(2,", "\"val2\")"
        ));

        InsertStatement statement = new InsertFactory().fromTokens(tokens);
        List<String> columns = statement.getColumnsClause().getColumns();
        List<List<Literal>> values = statement.getValuesClause().getValues();

        assertEquals("table1", statement.getTableName());
        assertEquals(2, columns.size());
        assertEquals("col1", columns.get(0));
        assertEquals("col2", columns.get(1));
        assertEquals(2, values.size());
        assertEquals("\"val1\"", values.get(0).get(0).toString());
        assertEquals("1", values.get(0).get(1).toString());
        assertEquals("2", values.get(1).get(0).toString());
        assertEquals("\"val2\"", values.get(1).get(1).toString());
    }

    @Test
    public void fromTokensMissingValuesClause() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
            "INTO", "table1", "(col1,", "col2", ")", "VALUES"
        ));

        assertThrows(
            SyntaxError.class,
            () -> new InsertFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensMissingColumnsClause() {
        Queue<String> tokens = new LinkedList<>(List.of(
            "INTO", "table1", "VALUES", "(\"val1\",1)", "(2,", "\"val2\")"
        ));

        assertThrows(
            SyntaxError.class,
            () -> new InsertFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensMissingKeyword() {
        Queue<String> tokens = new LinkedList<>(List.of(
            "INTO", "table1", "(col1,", "col2", ")", "(\"val1\",1)", "(2,", "\"val2\")"
        ));

        assertThrows(
            SyntaxError.class,
            () -> new InsertFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensMissingTableName() {
        Queue<String> tokens = new LinkedList<>(List.of(
            "INTO", "(col1,", "col2", ")", "VALUES", "(\"val1\",1)", "(2,", "\"val2\")"
        ));

        assertThrows(
            SyntaxError.class,
            () -> new InsertFactory().fromTokens(tokens)
        );

    }

    @Test
    public void fromTokensDifferentLengthOfTheColumnsClauseAndValueTuples() {
        Queue<String> tokens = new LinkedList<>(List.of(
            "INTO", "table1", "(col1", ")", "VALUES", "(\"val1\",1)", "(2,", "\"val2\")"
        ));

        assertThrows(
            SyntaxError.class,
            () -> new InsertFactory().fromTokens(tokens)
        );

    }

    @Test
    public void fromTokensMultipleConsecutiveSpaces() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
            "", "INTO", "", "", "table1", "(col1,", "col2", ")", "", "VALUES", "", "(\"val1\",1),", "(2,", "\"val2\")"
        ));

        InsertStatement statement = new InsertFactory().fromTokens(tokens);
        List<String> columns = statement.getColumnsClause().getColumns();
        List<List<Literal>> values = statement.getValuesClause().getValues();

        assertEquals("table1", statement.getTableName());
        assertEquals(2, columns.size());
        assertEquals("col1", columns.get(0));
        assertEquals("col2", columns.get(1));
        assertEquals(2, values.size());
        assertEquals("\"val1\"", values.get(0).get(0).toString());
        assertEquals("1", values.get(0).get(1).toString());
        assertEquals("2", values.get(1).get(0).toString());
        assertEquals("\"val2\"", values.get(1).get(1).toString());
    }
}