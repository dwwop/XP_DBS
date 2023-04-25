package core.parsing.tree.clauses.conditions;

import core.db.table.Row;

public abstract class Condition {
    public abstract boolean satisfiedOnRow(Row row);
}
