package core.parsing.tree.clauses.conditions;

import java.util.Objects;

public class NotCondition extends Condition {
    private final Condition condition;


    public NotCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return
                "NotCondition: " + condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotCondition that = (NotCondition) o;
        return Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition);
    }
}
