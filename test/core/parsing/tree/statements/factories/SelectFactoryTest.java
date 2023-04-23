package core.parsing.tree.statements.factories;

import core.parsing.Parser;
import core.parsing.tree.clauses.LimitClause;
import core.parsing.tree.clauses.OrderByClause;
import core.parsing.tree.clauses.SelectClause;
import core.parsing.tree.clauses.WhereClause;
import core.parsing.tree.clauses.conditions.AndCondition;
import core.parsing.tree.clauses.conditions.Expression;
import core.parsing.tree.clauses.conditions.NotCondition;
import core.parsing.tree.clauses.conditions.OrCondition;
import core.parsing.tree.statements.SelectStatement;
import core.parsing.util.KeywordConsumer;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SelectFactoryTest {

    @Test
    public void selectAllTest() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT * FROM table_name";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(true),
                        null,
                        null,
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void WrongSelectAllSyntax() {
        Parser parser = new Parser();
        String query = "SELECT* FROM table_name";
        assertThrows(SyntaxError.class, () -> parser.parse(query));
    }

    @Test
    public void MissingFrom() {
        Parser parser = new Parser();
        String query = "SELECT * table_name";
        assertThrows(TokenError.class, () -> parser.parse(query));
    }

    @Test
    public void MissingWholeFrom() {
        Parser parser = new Parser();
        String query = "SELECT *";
        assertThrows(EndOfFileError.class, () -> parser.parse(query));
    }

    @Test
    public void MissingColumns() {
        Parser parser = new Parser();
        String query = "SELECT FROM table_name";
        assertThrows(TokenError.class, () -> parser.parse(query));
    }

    @Test
    public void selectOneColumnTest() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        null,
                        null,
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }


    @Test
    public void selectTwoColumnsTest() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name1, column_name2 FROM table_name";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name1", "column_name2")),
                        null,
                        null,
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectMultipleColumnsTest() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name1, column_name2 ,column_name3 , column_name4 FROM table_name";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name1", "column_name2", "column_name3", "column_name4")),
                        null,
                        null,
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }


    @Test
    public void SelectMultipleCommas() {
        Parser parser = new Parser();
        String query = "SELECT column_name1, ,column_name2 FROM table_name";
        assertThrows(TokenError.class, () -> parser.parse(query));
    }


    @Test
    public void SelectCommaAtEndOfColumnList() {
        Parser parser = new Parser();
        String query = "SELECT column_name1, column_name2, FROM table_name";
        assertThrows(TokenError.class, () -> parser.parse(query));
    }


    @Test
    public void selectOrderByOneColumn() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        null,
                        new OrderByClause(Map.of("column_name", KeywordConsumer.Keyword.ASC)),
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectOrderByOneColumnDESC() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name DESC";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        null,
                        new OrderByClause(Map.of("column_name", KeywordConsumer.Keyword.DESC)),
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectOrderByMultipleColumns() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name1 DESC, column_name2, column_name3";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        null,
                        new OrderByClause(Map.of("column_name1", KeywordConsumer.Keyword.DESC, "column_name2", KeywordConsumer.Keyword.ASC, "column_name3", KeywordConsumer.Keyword.ASC)),
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectOrderByMultipleColumnsASCDESCDESC() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name1 ASC, column_name2 DESC, column_name3 DESC";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        null,
                        new OrderByClause(Map.of("column_name1", KeywordConsumer.Keyword.ASC, "column_name2", KeywordConsumer.Keyword.DESC, "column_name3", KeywordConsumer.Keyword.DESC)),
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectOrderByWrongASC() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name1 table_name LIMIT";
        assertThrows(SyntaxError.class, () -> parser.parse(query));
    }

    @Test
    public void selectOrderByMultipleCommas() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name1 ,, column_name2";
        assertThrows(TokenError.class, () -> parser.parse(query));
    }

    @Test
    public void selectLimit() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name LIMIT 10";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        null,
                        null,
                        new LimitClause(10, 0));

        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectLimitOffset() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name LIMIT 10 OFFSET 10";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        null,
                        null,
                        new LimitClause(10, 10));
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectSimpleExpr() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE column_name = value";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        new WhereClause(new Expression("column_name", "=", "value")),
                        null,
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectSimpleExprStr() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE column_name >= \"value\"";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        new WhereClause(new Expression("column_name", ">=", "value")),
                        null,
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectAnd() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE column_name <= \"value\" AND column_name != \"value\"";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        new WhereClause(new AndCondition(new Expression("column_name", "<=", "value"), new Expression("column_name", "!=", "value"))),
                        null,
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectNotBrackets() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE NOT ( column_name > \"value\" AND column_name < \"value\" )";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        new WhereClause(new NotCondition(new AndCondition(new Expression("column_name", ">", "value"), new Expression("column_name", "<", "value")))),
                        null,
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectComplexBrackets() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE ((NOT(column_name = \"value\" AND column_name = \"value\" )) OR(column_name = value) )";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        new WhereClause(new OrCondition(new NotCondition(new AndCondition(new Expression("column_name", "=", "value"), new Expression("column_name", "=", "value"))), new Expression("column_name", "=", "value"))),
                        null,
                        null);
        assertEquals(expectedSelectStatement, parser.parse(query));
    }

    @Test
    public void selectALLClauses() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE (column_name = \"value\") ORDER BY column_name DESC LIMIT 10 OFFSET 5";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        new WhereClause(new Expression("column_name", "=", "value")),
                        new OrderByClause(Map.of("column_name", KeywordConsumer.Keyword.DESC)),
                        new LimitClause(10, 5));
        assertEquals(expectedSelectStatement, parser.parse(query));
    }
}