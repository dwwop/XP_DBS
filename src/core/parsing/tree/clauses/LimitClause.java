package core.parsing.tree.clauses;

public class LimitClause extends Clause {
    Integer numberRows;
    Integer offsetValue;

    public LimitClause(Integer numberRows, Integer offsetValue) {
        this.numberRows = numberRows;
        this.offsetValue = offsetValue;
    }

    public Integer getNumberRows() {
        return numberRows;
    }

    public Integer getOffsetValue() {
        return offsetValue;
    }

}
