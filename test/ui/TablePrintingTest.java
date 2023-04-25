package ui;

import core.db.table.ColumnDefinition;
import core.db.table.Row;
import core.db.table.Schema;
import core.db.table.Table;
import core.db.types.IntegerLiteral;
import core.db.types.Literal;
import core.db.types.StringLiteral;
import exceptions.DatabaseError;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TablePrintingTest {

    @Test
    public void tableNullTest() {
        Console cs = new Console();
        Throwable exception = assertThrows(InvalidObjectException.class, () -> cs.returnTableString(null));
        assertEquals("Table invalid", exception.getMessage());
    }

    @Test
    public void tableNullSchema() {
        Console cs = new Console();
        Table testTable = new Table(null);
        Throwable exception = assertThrows(InvalidObjectException.class, () -> cs.returnTableString(testTable));
        assertEquals("Schema invalid", exception.getMessage());
    }

    @Test
    public void tableEmptySchema() {
        Console cs = new Console();
        Schema testSchema = new Schema();
        Table testTable = new Table(testSchema);
        Throwable exception = assertThrows(InvalidObjectException.class, () -> cs.returnTableString(testTable));
        assertEquals("Schema invalid", exception.getMessage());
    }

    @Test
    public void emptyTableFullSchema() throws InvalidObjectException, DatabaseError {
        Console cs = new Console();
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("ahojRiadokJeden", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDva", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDvaTRISTYRI", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("a", new ColumnDefinition(Literal.Type.String, new HashSet<>()));


        Table testTable = new Table(testSchema);
        assertEquals("\n-------------------------------------------------------\n" +
                "| a | ahojRiadokJeden | RiadokDvaTRISTYRI | RiadokDva |" +
                "\n-------------------------------------------------------\n", cs.returnTableString(testTable));
    }

    @Test
    public void oneRecordIncompleteTableFullSchema() throws InvalidObjectException, DatabaseError {
        Console cs = new Console();
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("ahojRiadokJeden", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDva", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDvaTRISTYRI", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("a", new ColumnDefinition(Literal.Type.String, new HashSet<>()));

        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("RiadokDva", new StringLiteral("atuhu"));
        vals.put("ahojRiadokJeden", new IntegerLiteral(23));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));

        assertEquals("\n-------------------------------------------------------\n" +
                "| a | ahojRiadokJeden | RiadokDvaTRISTYRI | RiadokDva |" +
                "\n-------------------------------------------------------\n" +
                "|   | 23              |                   | atuhu     |" +
                "\n-------------------------------------------------------\n", cs.returnTableString(testTable));
    }

    @Test
    public void oneRecordCompleteTableFullSchema() throws InvalidObjectException, DatabaseError {
        Console cs = new Console();
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("ahojRiadokJeden", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDva", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDvaTRISTYRI", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("a", new ColumnDefinition(Literal.Type.String, new HashSet<>()));

        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("RiadokDva", new StringLiteral("atuhu"));
        vals.put("ahojRiadokJeden", new IntegerLiteral(23));
        vals.put("a", new StringLiteral("1"));
        vals.put("RiadokDvaTRISTYRI", new StringLiteral("trystiri"));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));

        assertEquals("\n-------------------------------------------------------\n" +
                "| a | ahojRiadokJeden | RiadokDvaTRISTYRI | RiadokDva |" +
                "\n-------------------------------------------------------\n" +
                "| 1 | 23              | trystiri          | atuhu     |" +
                "\n-------------------------------------------------------\n", cs.returnTableString(testTable));
    }

    @Test
    public void oneRecordCompleteTableFullSchemaRecordLargerThanColName() throws InvalidObjectException, DatabaseError {
        Console cs = new Console();
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("ahojRiadokJeden", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDva", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDvaTRISTYRI", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("a", new ColumnDefinition(Literal.Type.String, new HashSet<>()));

        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("RiadokDva", new StringLiteral("atuhu"));
        vals.put("ahojRiadokJeden", new IntegerLiteral(23));
        vals.put("a", new StringLiteral("ahoj"));
        vals.put("RiadokDvaTRISTYRI", new StringLiteral("trystiri"));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));

        assertEquals("\n----------------------------------------------------------\n" +
                "| a    | ahojRiadokJeden | RiadokDvaTRISTYRI | RiadokDva |" +
                "\n----------------------------------------------------------\n" +
                "| ahoj | 23              | trystiri          | atuhu     |" +
                "\n----------------------------------------------------------\n", cs.returnTableString(testTable));
    }

    @Test
    public void oneRecordCompleteTableFullSchemaRecordLargerThanColNameAndOverflow() throws InvalidObjectException, DatabaseError {
        Console cs = new Console();
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("ahojRiadokJeden", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDva", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDvaTRISTYRI", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("a", new ColumnDefinition(Literal.Type.String, new HashSet<>()));

        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("RiadokDva", new StringLiteral("atuhu"));
        vals.put("ahojRiadokJeden", new IntegerLiteral(23));
        vals.put("a", new StringLiteral("ahoj"));
        vals.put("RiadokDvaTRISTYRI", new StringLiteral("abcdefghijklmnopqrstuvwxyz0123456789"));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));

        assertEquals("\n--------------------------------------------------------------------------\n" +
                "| a    | ahojRiadokJeden | RiadokDvaTRISTYRI                 | RiadokDva |\n" +
                "--------------------------------------------------------------------------\n" +
                "| ahoj | 23              | abcdefghijklmnopqrstuvwxyz0123456 | atuhu     |\n" +
                "|      |                 | 789                               |           |\n" +
                "--------------------------------------------------------------------------\n", cs.returnTableString(testTable));
    }

    @Test
    public void multipleRecordCompleteTableEmptySchema() throws DatabaseError {
        Console cs = new Console();
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("ahojRiadokJeden", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDva", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDvaTRISTYRI", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("a", new ColumnDefinition(Literal.Type.String, new HashSet<>()));


        Table testTable = new Table(null);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("RiadokDva", new StringLiteral("atuhu"));
        vals.put("ahojRiadokJeden", new IntegerLiteral(23));
        vals.put("a", new StringLiteral("ahoj"));
        vals.put("RiadokDvaTRISTYRI", new StringLiteral("abcdefghijklmnopqrstuvwxyz0123456789"));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));

        vals = new HashMap<>();
        vals.put("RiadokDvaTRISTYRI", new StringLiteral("yee"));
        vals.put("a", new StringLiteral("joj"));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));

        Throwable exception = assertThrows(InvalidObjectException.class, () -> cs.returnTableString(testTable));
        assertEquals("Schema invalid", exception.getMessage());
    }

    @Test
    public void multipleRecordCompleteTableInvalidSchema() throws DatabaseError {
        Console cs = new Console();
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("ahojRiadokJeden", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDva", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("aaaaa", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("a", new ColumnDefinition(Literal.Type.String, new HashSet<>()));

        Table testTable = new Table(null);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("RiadokDva", new StringLiteral("atuhu"));
        vals.put("ahojRiadokJeden", new IntegerLiteral(23));
        vals.put("a", new StringLiteral("ahoj"));
        vals.put("RiadokDvaTRISTYRI", new StringLiteral("abcdefghijklmnopqrstuvwxyz0123456789"));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));

        vals = new HashMap<>();
        vals.put("RiadokDvaTRISTYRI", new StringLiteral("yee"));
        vals.put("a", new StringLiteral("joj"));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));

        Throwable exception = assertThrows(InvalidObjectException.class, () -> cs.returnTableString(testTable));
        assertEquals("Schema invalid", exception.getMessage());
    }

    @Test
    public void multipleRecordCompleteTableFullSchemaRecordLargerThanColNameAndOverflow() throws InvalidObjectException, DatabaseError {
        Console cs = new Console();
        Schema testSchema = new Schema();
        testSchema.setColumnDefinition("ahojRiadokJeden", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDva", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("RiadokDvaTRISTYRI", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
        testSchema.setColumnDefinition("a", new ColumnDefinition(Literal.Type.String, new HashSet<>()));

        Table testTable = new Table(testSchema);

        Map<String, Literal> vals = new HashMap<>();
        vals.put("RiadokDva", new StringLiteral("atuhu"));
        vals.put("ahojRiadokJeden", new IntegerLiteral(23));
        vals.put("a", new StringLiteral("ahoj"));
        vals.put("RiadokDvaTRISTYRI", new StringLiteral("abcdefghijklmnopqrstuvwxyz0123456789"));
        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));

        vals = new HashMap<>();
        vals.put("RiadokDvaTRISTYRI", new StringLiteral("yee"));
        vals.put("ahojRiadokJeden", new IntegerLiteral(123323));
        vals.put("a", new StringLiteral("joj"));
        vals.put("RiadokDva", new StringLiteral("wuatatatata"));
        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));

        assertEquals("\n----------------------------------------------------------------------------\n" +
                "| a    | ahojRiadokJeden | RiadokDvaTRISTYRI                 | RiadokDva   |\n" +
                "----------------------------------------------------------------------------\n" +
                "| ahoj | 23              | abcdefghijklmnopqrstuvwxyz0123456 | atuhu       |\n" +
                "|      |                 | 789                               |             |\n" +
                "----------------------------------------------------------------------------\n" +
                "| joj  | 123323          | yee                               | wuatatatata |\n" +
                "----------------------------------------------------------------------------\n", cs.returnTableString(testTable));
    }
}
