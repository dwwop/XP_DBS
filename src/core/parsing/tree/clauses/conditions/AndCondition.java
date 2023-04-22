package core.parsing.tree.clauses.conditions;

import java.util.Objects;

public class AndCondition extends Condition {
    private final Condition firstCondition;
    private final Condition secondCondition;

    public AndCondition(Condition firstCondition, Condition secondCondition) {
        this.firstCondition = firstCondition;
        this.secondCondition = secondCondition;
    }

    @Override
    public String toString() {
        return
                "AND ( firstCondition: " + firstCondition + "\n" +
                        "secondCondition: " + secondCondition + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AndCondition that = (AndCondition) o;
        return Objects.equals(firstCondition, that.firstCondition) && Objects.equals(secondCondition, that.secondCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstCondition, secondCondition);
    }
}
