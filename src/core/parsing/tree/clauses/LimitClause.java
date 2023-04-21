package core.parsing.tree.clauses;

import java.util.Objects;

public class LimitClause extends Clause {
    Integer numberRows;
    Integer offsetValue;

    public LimitClause(Integer numberRows, Integer offsetValue) {
        this.numberRows = numberRows;
        this.offsetValue = offsetValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LimitClause that = (LimitClause) o;
        return Objects.equals(numberRows, that.numberRows) && Objects.equals(offsetValue, that.offsetValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberRows, offsetValue);
    }

    @Override
    public String toString() {
        return
                "numberRows: " + numberRows + "\n" +
                        "offsetValue: " + offsetValue;
    }
}
