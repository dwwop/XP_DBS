package core.parsing.tree.clauses.conditions;

import core.db.table.Row;
import exceptions.DatabaseError;

public abstract class Condition {
    public abstract boolean satisfiedOnRow(Row row) throws DatabaseError;
}
