package core.parsing;

import core.parsing.tree.statements.*;
import exceptions.syntax.SyntaxError;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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
    public void parseCreate() throws SyntaxError {
        String query =
                "  Create  TABLE  table1 ( col1 str primary key, col2 int not null )";

        Statement statement = new Parser().parse(query);

        assertTrue(statement instanceof CreateTableStatement);
    }

    @Test
    public void parseInsert() throws SyntaxError {
        String query =
                "insERT INTO table1 (col1, col2 ) VALUES (\"val1\",1), (2, \"val2\")";

        Statement statement = new Parser().parse(query);

        assertTrue(statement instanceof InsertStatement);
    }

    @Test
    public void parseDelete() throws SyntaxError {
        String query =
                "  DELete frOM customers WheRE customerName = \"Alfreds Futterkiste\"";

        Statement statement = new Parser().parse(query);

        assertTrue(statement instanceof DeleteStatement);
    }

    @Test
    public void parseUpdate() throws SyntaxError {
        String query =
                " UPdaTE Customers\n" +
                        "SeT ContactName = \"Alfred Schmidt\", City= \"Frankfurt\"\t" +
                        "WHErE CustomerID = 1";

        Statement statement = new Parser().parse(query);

        assertTrue(statement instanceof UpdateStatement);
    }

    @Test
    public void parseSelect() throws SyntaxError {
        String query =
                "SELEct           column1 , column2,column3\n" +
                        "FrOM table_name\n" +
                        "WHERE con = \"val\" AND con2 = 9\n" +
                        "ORDER BY column, column2 ASC\n" +
                        "LIMIT 10 OFFSET 20";

        Statement statement = new Parser().parse(query);

        assertTrue(statement instanceof SelectStatement);
    }
}