package core.parsing.tree.statements.factories;

import core.parsing.tree.statements.CreateTableStatement;
import core.parsing.tree.statements.Statement;
import exceptions.syntax.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CreateFactoryTest {

    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
                SyntaxError.class,
                () -> new CreateFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "Table", "table1", "(", "col1", "str", "primary", "key,", "col2", "int", "not", "null", ")"
        ));

        Statement statement = new CreateFactory().fromTokens(tokens);

        assertTrue(statement instanceof CreateTableStatement);
    }

    @Test
    public void fromTokensMissingKeyword() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "table1", "(", "col1", "str", "primary", "key,", "col2", "int", "not", "null", ")"
        ));

        assertThrows(
                SyntaxError.class,
                () -> new CreateFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensInvalidKeyword() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "column", "table1", "(", "col1", "str", "primary", "key,", "col2", "int", "not", "null", ")"
        ));

        assertThrows(
                SyntaxError.class,
                () -> new CreateFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensMultipleConsecutiveSpaces() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "", "", "TABLE", "", "table1", "(", "col1", "str", "primary", "key,", "col2", "int", "not", "null", ")"
        ));

        Statement statement = new CreateFactory().fromTokens(tokens);

        assertTrue(statement instanceof CreateTableStatement);
    }
}