package core.parsing.tree.statements.factories;

import core.parsing.KeywordConsumer;
import core.parsing.Parser;
import core.parsing.tree.clauses.ColumnClause;
import core.parsing.tree.clauses.LimitClause;
import core.parsing.tree.clauses.OrderByClause;
import core.parsing.tree.statements.SelectStatement;
import exceptions.syntaxErrors.SyntaxError;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SelectFactoryTest {

    @Test
    public void selectAllTest() {
        Parser parser = new Parser();
        String query = "SELECT * FROM table_name";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new ColumnClause(true),
                        null,
                        null,
                        null);
        try {
            assertEquals(expectedSelectStatement, parser.parse(query));
        } catch (SyntaxError e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void selectOneColumnTest() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new ColumnClause(List.of("column_name")),
                        null,
                        null,
                        null);
        try {
            assertEquals(expectedSelectStatement, parser.parse(query));
        } catch (SyntaxError e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void selectMultipleColumnsTest() {
        Parser parser = new Parser();
        String query = "SELECT column_name1, column_name2 FROM table_name";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new ColumnClause(List.of("column_name1", "column_name2")),
                        null,
                        null,
                        null);
        try {
            assertEquals(expectedSelectStatement, parser.parse(query));
        } catch (SyntaxError e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void selectOrderByOneColumn() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new ColumnClause(List.of("column_name")),
                        null,
                        new OrderByClause(Map.of("column_name", KeywordConsumer.Keyword.ASC)),
                        null);
        try {
            assertEquals(expectedSelectStatement, parser.parse(query));
        } catch (SyntaxError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void selectOrderByOneColumnDESC() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name DESC";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new ColumnClause(List.of("column_name")),
                        null,
                        new OrderByClause(Map.of("column_name", KeywordConsumer.Keyword.DESC)),
                        null);
        try {
            assertEquals(expectedSelectStatement, parser.parse(query));
        } catch (SyntaxError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void selectOrderByMultipleColumns() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name1, column_name2";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new ColumnClause(List.of("column_name")),
                        null,
                        new OrderByClause(Map.of("column_name1", KeywordConsumer.Keyword.ASC, "column_name2", KeywordConsumer.Keyword.ASC)),
                        null);
        try {
            assertEquals(expectedSelectStatement, parser.parse(query));
        } catch (SyntaxError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void selectOrderByMultipleColumnsASCDESC() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name1 ASC, column_name2 DESC";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new ColumnClause(List.of("column_name")),
                        null,
                        new OrderByClause(Map.of("column_name1", KeywordConsumer.Keyword.ASC, "column_name2", KeywordConsumer.Keyword.DESC)),
                        null);
        try {
            assertEquals(expectedSelectStatement, parser.parse(query));
        } catch (SyntaxError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void selectLimit() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name LIMIT 10";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new ColumnClause(List.of("column_name")),
                        null,
                        null,
                        new LimitClause(10, 0));
        try {
            assertEquals(expectedSelectStatement, parser.parse(query));
        } catch (SyntaxError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void selectLimitOffset() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name LIMIT 10 OFFSET 10";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new ColumnClause(List.of("column_name")),
                        null,
                        null,
                        new LimitClause(10, 10));
        try {
            assertEquals(expectedSelectStatement, parser.parse(query));
        } catch (SyntaxError e) {
            throw new RuntimeException(e);
        }
    }
}