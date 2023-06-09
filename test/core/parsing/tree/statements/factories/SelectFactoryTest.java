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

        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause(), statement.getWhereClause());
        assertEquals(expectedSelectStatement.getOrderByClause(), statement.getOrderByClause());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
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

        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause(), statement.getWhereClause());
        assertEquals(expectedSelectStatement.getOrderByClause(), statement.getOrderByClause());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
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


        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause(), statement.getWhereClause());
        assertEquals(expectedSelectStatement.getOrderByClause(), statement.getOrderByClause());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
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


        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause(), statement.getWhereClause());
        assertEquals(expectedSelectStatement.getOrderByClause(), statement.getOrderByClause());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
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
                        new OrderByClause(List.of("column_name"), List.of(KeywordConsumer.Keyword.ASC)),
                        null);


        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause(), statement.getWhereClause());
        assertEquals(expectedSelectStatement.getOrderByClause().getColumns(), statement.getOrderByClause().getColumns());
        assertEquals(expectedSelectStatement.getOrderByClause().getOrders(), statement.getOrderByClause().getOrders());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
    }

    @Test
    public void selectOrderByOneColumnDESC() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name DESC";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        null,
                        new OrderByClause(List.of("column_name"), List.of(KeywordConsumer.Keyword.DESC)),
                        null);


        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause(), statement.getWhereClause());
        assertEquals(expectedSelectStatement.getOrderByClause().getColumns(), statement.getOrderByClause().getColumns());
        assertEquals(expectedSelectStatement.getOrderByClause().getOrders(), statement.getOrderByClause().getOrders());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
    }

    @Test
    public void selectOrderByMultipleColumns() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name1 DESC, column_name2, column_name3";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        null,
                        new OrderByClause(List.of("column_name1", "column_name2", "column_name3"), List.of(KeywordConsumer.Keyword.DESC, KeywordConsumer.Keyword.ASC, KeywordConsumer.Keyword.ASC)),
                        null);


        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause(), statement.getWhereClause());
        assertEquals(expectedSelectStatement.getOrderByClause().getColumns(), statement.getOrderByClause().getColumns());
        assertEquals(expectedSelectStatement.getOrderByClause().getOrders(), statement.getOrderByClause().getOrders());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
    }

    @Test
    public void selectOrderByMultipleColumnsASCDESCDESC() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY column_name1 ASC, column_name2 DESC, column_name3 DESC";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        null,
                        new OrderByClause(List.of("column_name1", "column_name2", "column_name3"), List.of(KeywordConsumer.Keyword.ASC, KeywordConsumer.Keyword.DESC, KeywordConsumer.Keyword.DESC)),
                        null);


        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause(), statement.getWhereClause());
        assertEquals(expectedSelectStatement.getOrderByClause().getColumns(), statement.getOrderByClause().getColumns());
        assertEquals(expectedSelectStatement.getOrderByClause().getOrders(), statement.getOrderByClause().getOrders());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
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


        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause(), statement.getWhereClause());
        assertEquals(expectedSelectStatement.getOrderByClause(), statement.getOrderByClause());
        assertEquals(expectedSelectStatement.getLimitClause().getNumberRows(), statement.getLimitClause().getNumberRows());
        assertEquals(expectedSelectStatement.getLimitClause().getOffsetValue(), statement.getLimitClause().getOffsetValue());
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

        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause(), statement.getWhereClause());
        assertEquals(expectedSelectStatement.getOrderByClause(), statement.getOrderByClause());
        assertEquals(expectedSelectStatement.getLimitClause().getNumberRows(), statement.getLimitClause().getNumberRows());
        assertEquals(expectedSelectStatement.getLimitClause().getOffsetValue(), statement.getLimitClause().getOffsetValue());
    }


    @Test
    public void wordsAfterSelect() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name SHOULDNT_CONSUME";

        assertThrows(SyntaxError.class, () -> parser.parse(query));
    }

    @Test
    public void wordsAfterSelectWhere() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE col = val SHOULDNT_CONSUME";

        assertThrows(SyntaxError.class, () -> parser.parse(query));
    }

    @Test
    public void wordsAfterSelectWhereNull() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE col IS NULL SHOULDNT_CONSUME";

        assertThrows(SyntaxError.class, () -> parser.parse(query));
    }

    @Test
    public void wordsAfterSelectOrderBy() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY col SHOULDNT_CONSUME";

        assertThrows(SyntaxError.class, () -> parser.parse(query));
    }

    @Test
    public void wordsAfterSelectOrderByDesc() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name ORDER BY col DESC SHOULDNT_CONSUME";

        assertThrows(SyntaxError.class, () -> parser.parse(query));
    }


    @Test
    public void wordsAfterSelectLimit() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name LIMIT 10  SHOULDNT_CONSUME";

        assertThrows(SyntaxError.class, () -> parser.parse(query));
    }

    @Test
    public void wordsAfterSelectOffset() {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name LIMIT 10 OFFSET 10 SHOULDNT_CONSUME";

        assertThrows(SyntaxError.class, () -> parser.parse(query));
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
        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause().getCondition(), statement.getWhereClause().getCondition());
        assertEquals(expectedSelectStatement.getOrderByClause(), statement.getOrderByClause());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
    }

    @Test
    public void selectSimpleExprStr() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE column_name>= \"value\"";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        new WhereClause(new Expression("column_name", ">=", "value")),
                        null,
                        null);

        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause().getCondition(), statement.getWhereClause().getCondition());
        assertEquals(expectedSelectStatement.getOrderByClause(), statement.getOrderByClause());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
    }

    @Test
    public void selectAnd() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE column_name <=\"value\" AND column_name != \"value\"";

        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        new WhereClause(new AndCondition(new Expression("column_name", "<=", "value"), new Expression("column_name", "!=", "value"))),
                        null,
                        null);

        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause().getCondition(), statement.getWhereClause().getCondition());
        assertEquals(expectedSelectStatement.getOrderByClause(), statement.getOrderByClause());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
    }

    @Test
    public void selectNotBrackets() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE NOT (column_name> \"value\" AND column_name <\"value\" )";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        new WhereClause(new NotCondition(new AndCondition(new Expression("column_name", ">", "value"), new Expression("column_name", "<", "value")))),
                        null,
                        null);
        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause().getCondition(), statement.getWhereClause().getCondition());
        assertEquals(expectedSelectStatement.getOrderByClause(), statement.getOrderByClause());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
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
        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause().getCondition(), statement.getWhereClause().getCondition());
        assertEquals(expectedSelectStatement.getOrderByClause(), statement.getOrderByClause());
        assertEquals(expectedSelectStatement.getLimitClause(), statement.getLimitClause());
    }

    @Test
    public void selectALLClauses() throws SyntaxError {
        Parser parser = new Parser();
        String query = "SELECT column_name FROM table_name WHERE (column_name = \"value\") ORDER BY column_name DESC LIMIT 10 OFFSET 5";
        SelectStatement expectedSelectStatement =
                new SelectStatement("table_name",
                        new SelectClause(List.of("column_name")),
                        new WhereClause(new Expression("column_name", "=", "value")),
                        new OrderByClause(List.of("column_name"), List.of(KeywordConsumer.Keyword.DESC)),
                        new LimitClause(10, 5));

        SelectStatement statement = (SelectStatement) parser.parse(query);

        assertEquals(expectedSelectStatement.getSelectClause().getColumnNames(), statement.getSelectClause().getColumnNames());
        assertEquals(expectedSelectStatement.getSelectClause().isAllColumns(), statement.getSelectClause().isAllColumns());
        assertEquals(expectedSelectStatement.getWhereClause().getCondition(), statement.getWhereClause().getCondition());
        assertEquals(expectedSelectStatement.getOrderByClause().getColumns(), statement.getOrderByClause().getColumns());
        assertEquals(expectedSelectStatement.getOrderByClause().getOrders(), statement.getOrderByClause().getOrders());
        assertEquals(expectedSelectStatement.getLimitClause().getNumberRows(), statement.getLimitClause().getNumberRows());
        assertEquals(expectedSelectStatement.getLimitClause().getOffsetValue(), statement.getLimitClause().getOffsetValue());
    }
}