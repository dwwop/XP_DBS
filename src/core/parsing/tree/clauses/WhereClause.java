package core.parsing.tree.clauses;

import core.parsing.tree.clauses.conditions.Condition;

import java.util.Objects;

public class WhereClause extends Clause {
    private final Condition condition;

    public WhereClause(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return
                "condition: " + condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhereClause that = (WhereClause) o;
        return Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition);
    }
}
