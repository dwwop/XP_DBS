package core.parsing.tree.clauses;

import java.util.List;
import java.util.Objects;

public class SelectClause extends Clause {
    private boolean allColumns;

    private List<String> columnNames;

    public SelectClause(boolean allColumns) {
        this.allColumns = allColumns;
    }


    public SelectClause(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectClause that = (SelectClause) o;
        return allColumns == that.allColumns && Objects.equals(columnNames, that.columnNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allColumns, columnNames);
    }

    @Override
    public String toString() {
        return
                "allColumns: " + allColumns + "\n" +
                        "columnNames: " + columnNames;
    }
}
