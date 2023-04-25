package core.parsing.tree.clauses;

import core.parsing.tree.clauses.conditions.Condition;

public class WhereClause extends Clause {
    private final Condition condition;

    public WhereClause(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }
}
