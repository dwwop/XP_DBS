package core.parsing.tree.clauses;

import core.db.types.Literal;

import java.util.Map;

public class SetClause extends Clause {

    private final Map<String, Literal> columnValues;

    public SetClause(Map<String, Literal> columnValues) {
        this.columnValues = columnValues;
    }

    public Map<String, Literal> getColumnValues() {
        return columnValues;
    }
}
