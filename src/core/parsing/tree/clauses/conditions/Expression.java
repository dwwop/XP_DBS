package core.parsing.tree.clauses.conditions;

import java.util.Objects;

public class Expression extends Condition {
    private String columnName;

    private String comparator;
    private String value;

    public Expression(String columnName, String comparator, String value) {
        this.columnName = columnName;
        this.comparator = comparator;
        this.value = value;
    }

    @Override
    public String toString() {
        return
                "columnName: '" + columnName + '\'' +
                        "comparator: '" + comparator + '\'' +
                        "value: '" + value + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return Objects.equals(columnName, that.columnName) && Objects.equals(comparator, that.comparator) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, comparator, value);
    }
}
