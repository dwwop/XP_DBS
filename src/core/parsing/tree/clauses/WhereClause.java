package core.parsing.tree.clauses;

import core.db.table.Row;
import core.parsing.tree.clauses.conditions.Condition;
import exceptions.DatabaseError;

public class WhereClause extends Clause {
    private final Condition condition;

    public WhereClause() {
        condition = null;
    }

    public WhereClause(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public boolean satisfiedOnRow(Row row) throws DatabaseError {
        if (condition == null)
            return true;
        return condition.satisfiedOnRow(row);
    }
}
