package core.db.table;

import core.db.types.IntegerLiteral;
import core.db.types.Literal;
import core.db.types.StringLiteral;
import core.parsing.tree.clauses.*;
import core.parsing.tree.clauses.conditions.Expression;
import core.parsing.tree.clauses.conditions.OrCondition;
import core.parsing.tree.statements.factories.CreateTableFactory;
import core.parsing.util.KeywordConsumer;
import exceptions.DatabaseError;
import exceptions.syntax.SyntaxError;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TableTest {
    @Test
    public void selectAllColumns() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new IntegerLiteral(1));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        vals.put("column2", new IntegerLiteral(2));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));

        SelectClause select = new SelectClause(true);
        Table resultTable = testTable.select(select);

        assertEquals(resultTable.getRows().size(), testTable.getRows().size());
        assertEquals(resultTable.getRows().get(new IntegerLiteral(1)).getValue("column1")
                , testTable.getRows().get(new IntegerLiteral(1)).getValue("column1"));
        assertEquals(resultTable.getRows().get(new IntegerLiteral(1)).getValue("column2")
                , testTable.getRows().get(new IntegerLiteral(1)).getValue("column2"));
    }

    @Test
    public void selectColumn2() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new IntegerLiteral(1));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        vals.put("column2", new IntegerLiteral(2));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));

        SelectClause select = new SelectClause(List.of("column2"));
        Table resultTable = testTable.select(select);

        assertEquals(resultTable.getRows().size(), testTable.getRows().size());
        assertEquals(1
                , resultTable.getRows().get(new IntegerLiteral(1)).getValue("column2").getValue());
        assertEquals(null
                , resultTable.getRows().get(new IntegerLiteral(1)).getValue("column1"));
    }

    @Test
    public void selectNonexistentColumn() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setPrimaryKeyColumn("column1");
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        testTable.getRows().put(new StringLiteral("a"), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        testTable.getRows().put(new StringLiteral("b"), new Row(vals));

        SelectClause selectClause = new SelectClause(List.of("column2"));
        assertThrows(DatabaseError.class, () -> testTable.select(selectClause));
    }



    @Test
    public void whereSimpleExpression() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new IntegerLiteral(1));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        vals.put("column2", new IntegerLiteral(2));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));

        WhereClause whereClause = new WhereClause(new Expression("column1", "=", "a"));
        Table resultTable = testTable.where(whereClause);

        assertEquals(1, resultTable.getRows().size());
        assertEquals(1
                , resultTable.getRows().get(new IntegerLiteral(1)).getValue("column2").getValue());
        assertEquals(null
                , resultTable.getRows().get(new IntegerLiteral(2)));
    }

    @Test
    public void whereOr() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new IntegerLiteral(1));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        vals.put("column2", new IntegerLiteral(2));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));

        WhereClause whereClause = new WhereClause(new OrCondition(new Expression("column2", "<", "2"),
                new Expression("column1", "=", "b")));
        Table resultTable = testTable.where(whereClause);

        assertEquals(2, resultTable.getRows().size());
        assertEquals(1
                , resultTable.getRows().get(new IntegerLiteral(1)).getValue("column2").getValue());
        assertEquals("b"
                , resultTable.getRows().get(new IntegerLiteral(2)).getValue("column1").getValue());
    }

    @Test
    public void unsatisfiedWhere() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setPrimaryKeyColumn("column1");
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        testTable.getRows().put(new StringLiteral("a"), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        testTable.getRows().put(new StringLiteral("b"), new Row(vals));

        WhereClause whereClause = new WhereClause(new Expression("column1", "=", "c"));
        Table resultTable = testTable.where(whereClause);

        assertEquals(0, resultTable.getRows().size());
    }

    @Test
    public void whereNonexistentColumn() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setPrimaryKeyColumn("column1");
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        testTable.getRows().put(new StringLiteral("a"), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        testTable.getRows().put(new StringLiteral("b"), new Row(vals));

        WhereClause whereClause = new WhereClause(new Expression("column2", "=", "c"));
        assertThrows(DatabaseError.class, () -> testTable.where(whereClause));
    }

    @Test
    public void orderBy() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new IntegerLiteral(1));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        vals.put("column2", new IntegerLiteral(2));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));
        assertEquals("a", testTable.getRows().entrySet().iterator().next().getValue().getValue("column1").getValue());

        OrderByClause orderByClause = new OrderByClause(List.of("column1"), List.of(KeywordConsumer.Keyword.DESC));
        Table resultTable = testTable.orderBy(orderByClause);

        assertEquals(2, resultTable.getRows().size());
        assertEquals("b", resultTable.getRows().entrySet().iterator().next().getValue().getValue("column1").getValue());
    }

    @Test
    public void orderByMultiple() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        testSchema.setColumnDefinition("column3", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new IntegerLiteral(1));
        vals.put("column3", new IntegerLiteral(3));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        vals.put("column2", new IntegerLiteral(2));
        vals.put("column3", new IntegerLiteral(2));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        vals.put("column2", new IntegerLiteral(3));
        vals.put("column3", new IntegerLiteral(1));
        testTable.getRows().put(new IntegerLiteral(3), new Row(vals));
        assertEquals("a", testTable.getRows().entrySet().iterator().next().getValue().getValue("column1").getValue());

        OrderByClause orderByClause = new OrderByClause(List.of("column1", "column3"), List.of(KeywordConsumer.Keyword.DESC, KeywordConsumer.Keyword.ASC));
        Table resultTable = testTable.orderBy(orderByClause);

        assertEquals(3, resultTable.getRows().size());
        assertEquals("b", resultTable.getRows().entrySet().iterator().next().getValue().getValue("column1").getValue());
        assertEquals(1, resultTable.getRows().entrySet().iterator().next().getValue().getValue("column3").getValue());
    }

    @Test
    public void orderByNonexistentColumn() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setPrimaryKeyColumn("column1");
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        testTable.getRows().put(new StringLiteral("a"), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        testTable.getRows().put(new StringLiteral("b"), new Row(vals));

        OrderByClause orderByClause = new OrderByClause(List.of("column2"), List.of(KeywordConsumer.Keyword.DESC));
        assertThrows(DatabaseError.class, () -> testTable.orderBy(orderByClause));
    }

    @Test
    public void limit() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("c"));
        testTable.getRows().put(new IntegerLiteral(3), new Row(vals));

        LimitClause limitClause = new LimitClause(2, 0);
        Table resultTable = testTable.limit(limitClause);

        assertEquals(2, resultTable.getRows().size());
    }

    @Test
    public void limitAndOffset() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("c"));
        testTable.getRows().put(new IntegerLiteral(3), new Row(vals));

        LimitClause limitClause = new LimitClause(1, 2);
        Table resultTable = testTable.limit(limitClause);

        assertEquals(1, resultTable.getRows().size());
        assertEquals("c", resultTable.getRows().entrySet().iterator().next().getValue().getValue("column1").getValue());
    }

    @Test
    public void insertNoPrimaryKey() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));

        ColumnsClause columnsClause = new ColumnsClause(List.of("column1"));
        ValuesClause valuesClause = new ValuesClause(List.of(List.of(new StringLiteral("b"))));

        assertThrows(DatabaseError.class, () -> testTable.insert(columnsClause, valuesClause));
    }

    @Test
    public void insert() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setPrimaryKeyColumn("column1");
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        testTable.getRows().put(new StringLiteral("a"), new Row(vals));

        ColumnsClause columnsClause = new ColumnsClause(List.of("column1"));
        ValuesClause valuesClause = new ValuesClause(List.of(List.of(new StringLiteral("b"))));

        Table resultTable = testTable.insert(columnsClause, valuesClause);

        assertEquals(2, resultTable.getRows().size());
    }

    @Test
    public void insertDifferentType() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setPrimaryKeyColumn("column1");
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        testTable.getRows().put(new StringLiteral("a"), new Row(vals));

        ColumnsClause columnsClause = new ColumnsClause(List.of("column1"));
        ValuesClause valuesClause = new ValuesClause(List.of(List.of(new IntegerLiteral(1))));

        assertThrows(DatabaseError.class, () -> testTable.insert(columnsClause, valuesClause));
    }

    @Test
    public void insertSamePrimaryKey() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setPrimaryKeyColumn("column1");
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        testTable.getRows().put(new StringLiteral("a"), new Row(vals));

        ColumnsClause columnsClause = new ColumnsClause(List.of("column1"));
        ValuesClause valuesClause = new ValuesClause(List.of(List.of(new StringLiteral("a"))));

        assertThrows(DatabaseError.class, () -> testTable.insert(columnsClause, valuesClause));
    }

    @Test
    public void insertDifferentTypeNotAsPrimaryKey() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setPrimaryKeyColumn("column1");
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new StringLiteral("A"));
        testTable.getRows().put(new StringLiteral("a"), new Row(vals));

        ColumnsClause columnsClause = new ColumnsClause(List.of("column1", "column2"));
        ValuesClause valuesClause = new ValuesClause(List.of(List.of(new StringLiteral("b")), List.of(new IntegerLiteral(1))));

        assertThrows(DatabaseError.class, () -> testTable.insert(columnsClause, valuesClause));
    }

    @Test
    public void insertToNonexistantColumn() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setPrimaryKeyColumn("column1");
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new StringLiteral("A"));
        testTable.getRows().put(new StringLiteral("a"), new Row(vals));

        ColumnsClause columnsClause = new ColumnsClause(List.of("column3", "column2"));
        ValuesClause valuesClause = new ValuesClause(List.of(List.of(new StringLiteral("b")), List.of(new StringLiteral("b"))));

        assertThrows(DatabaseError.class, () -> testTable.insert(columnsClause, valuesClause));
    }

    @Test
    public void delete() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new IntegerLiteral(1));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        vals.put("column2", new IntegerLiteral(2));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));

        WhereClause whereClause = new WhereClause(new Expression("column1", "=", "a"));
        Table resultTable = testTable.delete(whereClause);

        assertEquals(1, resultTable.getRows().size());
        assertEquals("b"
                , resultTable.getRows().get(new IntegerLiteral(2)).getValue("column1").getValue());
    }

    @Test
    public void update() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new IntegerLiteral(1));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        vals.put("column2", new IntegerLiteral(2));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));

        WhereClause whereClause = new WhereClause(new Expression("column1", "=", "a"));
        SetClause setClause = new SetClause(Map.of("column1", new StringLiteral("c")));
        Table resultTable = testTable.update(whereClause, setClause);

        assertEquals(2, resultTable.getRows().size());
        assertEquals("c"
                , resultTable.getRows().get(new IntegerLiteral(1)).getValue("column1").getValue());
    }

    @Test
    public void updatePrimaryKey() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        testSchema.setPrimaryKeyColumn("column1");
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new IntegerLiteral(1));
        testTable.getRows().put(new StringLiteral("a"), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        vals.put("column2", new IntegerLiteral(2));
        testTable.getRows().put(new StringLiteral("b"), new Row(vals));

        WhereClause whereClause = new WhereClause(new Expression("column1", "=", "a"));
        SetClause setClause = new SetClause(Map.of("column1", new StringLiteral("c")));
        Table resultTable = testTable.update(whereClause, setClause);

        assertEquals(2, resultTable.getRows().size());
        assertEquals("c"
                , resultTable.getRows().get(new StringLiteral("c")).getValue("column1").getValue());
    }

    @Test
    public void updateNonexistantColumn() throws DatabaseError {
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("column1", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("column2", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        testSchema.setPrimaryKeyColumn("column1");
        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("column1", new StringLiteral("a"));
        vals.put("column2", new IntegerLiteral(1));
        testTable.getRows().put(new StringLiteral("a"), new Row(vals));
        vals = new HashMap<>();
        vals.put("column1", new StringLiteral("b"));
        vals.put("column2", new IntegerLiteral(2));
        testTable.getRows().put(new StringLiteral("b"), new Row(vals));

        WhereClause whereClause = new WhereClause(new Expression("column1", "=", "a"));
        SetClause setClause = new SetClause(Map.of("column3", new StringLiteral("c")));

        assertThrows(DatabaseError.class, () -> testTable.update(whereClause, setClause));
    }
}