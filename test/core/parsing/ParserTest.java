package core.parsing;

import core.parsing.tree.statements.CreateTableStatement;
import core.parsing.tree.statements.InsertStatement;
import core.parsing.tree.statements.Statement;
import core.parsing.tree.statements.factories.CreateFactory;
import exceptions.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void parseEmpty() {
        String query = "";

        assertThrows(
            SyntaxError.class,
            () -> new Parser().parse(query)
        );
    }

    @Test
    public void parseOnlySpaces() {
        String query = "  ";

        assertThrows(
            SyntaxError.class,
            () -> new Parser().parse(query)
        );
    }

    @Test
    public void fromTokensOK() throws SyntaxError {
        String query =
            "insERT INTO table1 (col1, col2 ) VALUES (\"val1\",1), (2, \"val2\")";

        Statement statement = new Parser().parse(query);

        assertTrue(statement instanceof InsertStatement);
    }

    @Test
    public void fromTokensMissingKeyword() {
        String query =
            "FROM table1 where a = 1";

        assertThrows(
            SyntaxError.class,
            () -> new Parser().parse(query)
        );
    }

    @Test
    public void fromTokensInvalidKeyword() {
        String query =
            "unknown FROM table1 where a = 1";

        assertThrows(
            SyntaxError.class,
            () -> new Parser().parse(query)
        );
    }

    @Test
    public void parseMultipleConsecutiveSpaces() throws SyntaxError {
        String query =
            "  Create  TABLE  table1 ( col1 str primary key, col2 int not null )";

        Statement statement = new Parser().parse(query);

        assertTrue(statement instanceof CreateTableStatement);
    }
}