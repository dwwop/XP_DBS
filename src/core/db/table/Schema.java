package core.db.table;

import exceptions.DatabaseError;

import java.util.HashMap;
import java.util.Map;

public class Schema {

    private final Map<String, ColumnDefinition> columns = new HashMap<>();
    private String primaryKeyColumn;

    public boolean hasColumn(String column) {
        return columns.containsKey(column);
    }

    private void validateColumnExistence(String column) throws DatabaseError {
        if (!hasColumn(column)) {
            throw new DatabaseError("Column '" + column + "' does not exist.");
        }
    }

    public ColumnDefinition getColumnDefinition(String column) throws DatabaseError {
        validateColumnExistence(column);

        return columns.get(column);
    }

    public void setColumnDefinition(String column, ColumnDefinition definition) throws DatabaseError {
        if (violatesSinglePrimaryKeyRestriction(column, definition)) {
            throw new DatabaseError("Multiple primary-key columns not allowed.");
        }

        columns.put(column, definition);

        if (definition.hasConstraint(ColumnDefinition.Constraint.PrimaryKey)) {
            setPrimaryKeyColumn(column);
        }
    }

    public void addColumnConstraint(String column, ColumnDefinition.Constraint constraint) throws DatabaseError {
        validateColumnExistence(column);

        columns.get(column).addConstraint(constraint);
    }

    public void removeColumnConstraint(String column, ColumnDefinition.Constraint constraint) throws DatabaseError {
        validateColumnExistence(column);

        columns.get(column).removeConstraint(constraint);
    }

    public String getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public void setPrimaryKeyColumn(String primaryKeyColumn) throws DatabaseError {
        validateColumnExistence(primaryKeyColumn);

        if (this.primaryKeyColumn != null) {
            removeColumnConstraint(this.primaryKeyColumn, ColumnDefinition.Constraint.PrimaryKey);
        }

        this.primaryKeyColumn = primaryKeyColumn;
        addColumnConstraint(this.primaryKeyColumn, ColumnDefinition.Constraint.PrimaryKey);
    }

    private boolean violatesSinglePrimaryKeyRestriction(String column, ColumnDefinition definition) {
        return primaryKeyColumn != null
                && definition.hasConstraint(ColumnDefinition.Constraint.PrimaryKey)
                && !primaryKeyColumn.equals(column);
    }

    public Map<String, ColumnDefinition> getColumns() {
        return columns;
    }
}
