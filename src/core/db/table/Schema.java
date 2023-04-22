package core.db.table;

import java.util.HashMap;
import java.util.Map;

public class Schema {

    private Map<String, ColumnDefinition> columns = new HashMap<>();
    private String primaryKeyColumn;

    public Map<String, ColumnDefinition> getColumns() {
        return columns;
    }

    public String getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public void setColumns(Map<String, ColumnDefinition> columns) {
        this.columns = columns;
    }

    public void setPrimaryKeyColumn(String primaryKeyColumn) {
        this.primaryKeyColumn = primaryKeyColumn;
    }
}
