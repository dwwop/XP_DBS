package core.parsing.tree.clauses.conditions;

import core.db.table.Row;
import core.db.types.IntegerLiteral;
import core.db.types.Literal;
import core.db.types.StringLiteral;

import java.util.Objects;

public class Expression extends Condition {
    private final String columnName;

    private final String comparator;
    private final String value;

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

    @Override
    public boolean satisfiedOnRow(Row row) {
        Literal rowValue = row.getValue(columnName);
        Literal expectedValue;
        if (rowValue instanceof IntegerLiteral){
            expectedValue = new IntegerLiteral(Integer.valueOf(value));
        } else {
            expectedValue = new StringLiteral(value);
        }

        return switch (comparator) {
            case "=" -> rowValue == expectedValue;
            case "!=" -> !(rowValue == expectedValue);
            case "<" -> rowValue.compareTo(expectedValue) < 0;
            case ">" -> rowValue.compareTo(expectedValue) > 0;
            case "<=" -> rowValue.compareTo(expectedValue) <= 0;
            case ">=" -> rowValue.compareTo(expectedValue) >= 0;
            default -> false;
        };
    }
}
