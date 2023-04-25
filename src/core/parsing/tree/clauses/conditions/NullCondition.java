package core.parsing.tree.clauses.conditions;

import java.util.Objects;

public class NullCondition extends Condition {
    private final String columnName;

    public NullCondition(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        return
                "columnName: '" + columnName + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NullCondition that = (NullCondition) o;
        return Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName);
    }
}
