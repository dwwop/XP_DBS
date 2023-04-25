package core.parsing.tree.clauses;

import core.db.table.Row;
import core.parsing.tree.clauses.conditions.Condition;

public class WhereClause extends Clause {
    private final Condition condition;

    public WhereClause(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public boolean satisfiedOnRow(Row row) {
        return condition.satisfiedOnRow(row);
    }
}
