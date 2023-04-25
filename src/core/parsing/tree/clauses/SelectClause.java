package core.parsing.tree.clauses;

import java.util.List;

public class SelectClause extends Clause {
    private boolean allColumns;

    private List<String> columnNames;

    public SelectClause(boolean allColumns) {
        this.allColumns = allColumns;
    }


    public SelectClause(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public boolean isAllColumns() {
        return allColumns;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public boolean getAllColumns() {
        return allColumns;
    }
}
