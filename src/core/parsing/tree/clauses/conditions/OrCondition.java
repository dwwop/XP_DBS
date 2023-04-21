package core.parsing.tree.clauses.conditions;

import java.util.Objects;

public class OrCondition extends Condition {

    private final Condition firstCondition;
    private Condition secondCondition;

    public OrCondition(Condition firstCondition, Condition secondCondition) {
        this.firstCondition = firstCondition;
        this.secondCondition = secondCondition;
    }

    @Override
    public String toString() {
        return
                "OR (firstCondition: " + firstCondition + "\n" +
                        "secondCondition: " + secondCondition +")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrCondition that = (OrCondition) o;
        return Objects.equals(firstCondition, that.firstCondition) && Objects.equals(secondCondition, that.secondCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstCondition, secondCondition);
    }
}
