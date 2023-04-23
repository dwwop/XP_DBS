package core.parsing.tree.statements.factories;

import core.db.table.ColumnDefinition;
import core.db.table.Schema;
import core.db.types.Literal;
import core.parsing.tree.statements.CreateTableStatement;
import exceptions.DatabaseError;
import exceptions.syntax.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

public class CreateTableFactoryTest {

    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
                SyntaxError.class,
                () -> new CreateTableFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensOK() throws SyntaxError, DatabaseError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "table1", "(", "col1", "str", "primary", "key,", "col2", "int", "not", "null", ")"
        ));

        CreateTableStatement statement = new CreateTableFactory().fromTokens(tokens);
        Schema schema = statement.getSchema();

        assertEquals("table1", statement.getTableName());
        assertTrue(schema.hasColumn("col1"));

        ColumnDefinition def1 = schema.getColumnDefinition("col1");
        assertEquals(Literal.Type.String, def1.getDataType());
        assertTrue(def1.hasConstraint(ColumnDefinition.Constraint.PrimaryKey));
        assertEquals("col1", schema.getPrimaryKeyColumn());

        ColumnDefinition def2 = schema.getColumnDefinition("col2");
        assertEquals(Literal.Type.Integer, def2.getDataType());
        assertTrue(def2.hasConstraint(ColumnDefinition.Constraint.NotNull));
    }

    @Test
    public void fromTokensMissingSchema() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "table1"
        ));

        assertThrows(
                SyntaxError.class,
                () -> new CreateTableFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensMultipleColumnsWithSameName() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "table1", "(", "col1", "str", "primary", "key,", "col1", "int", "not", "null", ")"
        ));

        assertThrows(
                SyntaxError.class,
                () -> new CreateTableFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensMultipleColumnsWithPrimaryKey() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "table1", "(", "col1", "str", "primary", "key,", "col2", "int", "primary", "key", ")"
        ));

        assertThrows(
                SyntaxError.class,
                () -> new CreateTableFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensMissingDataType() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "table1", "(", "col1", "primary", "key,", "col2", "int", "not", "null", ")"
        ));

        assertThrows(
                SyntaxError.class,
                () -> new CreateTableFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensRepetingConstraint() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "table1", "(", "col1", "str", "primary", "key,", "col2", "int", "not", "null", "not", "null", ")"
        ));

        assertThrows(
                SyntaxError.class,
                () -> new CreateTableFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensMissingTableName() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "(", "col1", "str", "primary", "key,", "col2", "int", "not", "null", ")"
        ));

        assertThrows(
                SyntaxError.class,
                () -> new CreateTableFactory().fromTokens(tokens)
        );

    }

    @Test
    public void fromTokensMultipleConsecutiveSpaces() throws SyntaxError, DatabaseError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "table1", "", "(", "col1", "", "str", "", "primary", "key,", "", "", "col2", "int", "not", "null", "", ")", ""
        ));

        CreateTableStatement statement = new CreateTableFactory().fromTokens(tokens);
        Schema schema = statement.getSchema();

        assertEquals("table1", statement.getTableName());
        assertTrue(schema.hasColumn("col1"));

        ColumnDefinition def1 = schema.getColumnDefinition("col1");
        assertEquals(Literal.Type.String, def1.getDataType());
        assertTrue(def1.hasConstraint(ColumnDefinition.Constraint.PrimaryKey));

        ColumnDefinition def2 = schema.getColumnDefinition("col2");
        assertEquals(Literal.Type.Integer, def2.getDataType());
        assertTrue(def2.hasConstraint(ColumnDefinition.Constraint.NotNull));
    }
}