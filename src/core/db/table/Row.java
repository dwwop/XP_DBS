package core.db.table;

import core.db.types.Literal;

import java.util.Map;

public class Row {

    private final Map<String, Literal> values;

    public Row(Map<String, Literal> values) {
        this.values = values;
    }

    public void setValue(String column, Literal value) {
        // TODO: check if value is of correct type somewhere

        if (values.containsKey(column)) {
            values.put(column, value);
        }
    }

    private Literal getValue(String column) {
        // TODO: return null Literal of correct type instead of plain null?

        return values.get(column);
    }
}
