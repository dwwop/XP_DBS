package core.parsing.tree.clauses;

import java.util.List;

public class ColumnsClause extends Clause {

    private final List<String> columns;

    public ColumnsClause(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }
}
