package core.db.table;

import core.db.types.Literal;

import java.util.Map;

public class Row {

    private final Map<String, Literal> values;

    public Row(Map<String, Literal> values) {
        this.values = values;
    }

    public void setValue(String column, Literal value) {
        values.put(column, value);
    }

    public Literal getValue(String column) {
        return values.get(column);
    }

    public boolean hasColumn(String column){
        return values.containsKey(column);
    }
}
