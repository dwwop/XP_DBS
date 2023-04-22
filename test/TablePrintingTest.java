import core.db.table.ColumnDefinition;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TablePrintingTest {

    @Test
    public void test() {
        assertTrue(true);
    }
}


//    Map<String, ColumnDefinition> testColumns = new HashMap<>();
//        testColumns.put("ahojRiadokJeden", new ColumnDefinition(Literal.Type.Integer, new HashSet<>()));
//        testColumns.put("RiadokDva", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
//        testColumns.put("RiadokDvaTRISTYRI", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
//        testColumns.put("a", new ColumnDefinition(Literal.Type.String, new HashSet<>()));
//
//
//        Schema testSchema = new Schema();
//        testSchema.setColumns(testColumns);
//
//        Table testTable = new Table(testSchema);
//
//        Map<String, Literal> vals = new HashMap<>();
//        vals.put("RiadokDva", new StringLiteral("atuhu"));
//        vals.put("ahojRiadokJeden", new IntegerLiteral(23));
//        testTable.getRows().put(new IntegerLiteral(1), new Row(vals));
//
//        vals = new HashMap<>();
//        vals.put("RiadokDvaTRISTYRI", new StringLiteral("CTIBOR ALE VSAK BNOIBNOIBOHABOAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHABOHAAAAAAAA"));
//        vals.put("a", new StringLiteral("joj"));
//        testTable.getRows().put(new IntegerLiteral(2), new Row(vals));