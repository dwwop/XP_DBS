package core.db.table;

import core.db.types.IntegerLiteral;
import core.db.types.Literal;
import core.parsing.tree.clauses.*;
import core.parsing.util.KeywordConsumer;
import exceptions.DatabaseError;

import java.util.*;

public class Table {

    private final Schema schema;

    private Map<Literal, Row> rows = new TreeMap<>();
    private Comparator<Literal> customComparator;

    public Table(Schema schema) {
        this.schema = schema;
    }

    public Table select(SelectClause selectClause) throws DatabaseError {
        if (selectClause.getAllColumns()) {
            return this;
        }

        Schema resultTableSchema = new Schema();

        for (String columnName : selectClause.getColumnNames()) {
            resultTableSchema.setColumnDefinition(columnName, schema.getColumnDefinition(columnName));
        }
        if (resultTableSchema.hasColumn(schema.getPrimaryKeyColumn())) {
            resultTableSchema.setPrimaryKeyColumn(schema.getPrimaryKeyColumn());
        }

        Table resultTable = new Table(resultTableSchema);

        for (Literal literal : rows.keySet()) {
            Row row = rows.get(literal);
            Row newRow = new Row(new HashMap<>());
            for (String column : selectClause.getColumnNames()) {
                newRow.setValue(column, row.getValue(column));
            }
            resultTable.addRow(literal, newRow);
        }

        return resultTable;
    }

    public Table where(WhereClause whereClause) throws DatabaseError {
        Table resultTable = new Table(this.schema);
        for (Literal key : rows.keySet()) {
            Row row = rows.get(key);
            if (whereClause.satisfiedOnRow(row)) {
                resultTable.rows.put(key, row);
            }
        }
        return resultTable;
    }

    public Table orderBy(OrderByClause orderByClause) throws DatabaseError{
        Table resultTable = new Table(this.schema);
        List<String> columns = orderByClause.getColumns();
        List<KeywordConsumer.Keyword> orders = orderByClause.getOrders();
        for (String column : columns){
            if (! schema.getColumns().keySet().contains(column)){
                throw new DatabaseError("Row doesn't have column named: " + column);
            }
        }

        resultTable.customComparator = (l1, l2) -> {
            Row row1 = rows.get(l1);
            Row row2 = rows.get(l2);
            for (int i = 0; i < columns.size(); i++) {
                int comparisonResult = row1.getValue(columns.get(i)).compareTo(row2.getValue(columns.get(i)));
                if (comparisonResult == 0) {
                    continue;
                }
                if (orders.get(i) == KeywordConsumer.Keyword.ASC) {
                    return comparisonResult;
                }
                return -1 * comparisonResult;
            }
            return 0;
        };

        resultTable.rows = new TreeMap<>(resultTable.customComparator);
        resultTable.rows.putAll(rows);

        return resultTable;
    }

    public Table limit(LimitClause limitClause) throws DatabaseError {
        Table resultTable = new Table(this.schema);
        resultTable.customComparator = customComparator;
        resultTable.rows = new TreeMap<>(customComparator);

        if (limitClause.getOffsetValue() < 0 || limitClause.getNumberRows() < 0){
            throw new DatabaseError("Limit or offset can't be < 0");
        }

        Integer offset = limitClause.getOffsetValue();
        Integer limit = limitClause.getNumberRows() + offset;
        int index = 0;
        for (Map.Entry<Literal, Row> entry : rows.entrySet()) {
            if (index >= offset && index < limit) {
                resultTable.rows.put(entry.getKey(), entry.getValue());
            }
            index++;
        }

        return resultTable;
    }

    public Table insert(ColumnsClause columnsClause, ValuesClause valuesClause) throws DatabaseError {
        Table resultTable = new Table(this.schema);
        resultTable.rows.putAll(rows);

        List<String> columns = columnsClause.getColumns();
        if (schema.getPrimaryKeyColumn() == null){
            throw new DatabaseError("Primary key is not set");
        }
        int primaryKeyIndex = columns.indexOf(schema.getPrimaryKeyColumn());
        if (primaryKeyIndex == -1) {
            throw new DatabaseError("Primary key column must have a value");
        }

        for (List<Literal> rowValues : valuesClause.getValues()) {
            Row newRow = new Row(new HashMap<>());
            for (int i = 0; i < rowValues.size(); i++) {
                resultTable.validateValue(columns.get(i), rowValues.get(i));
                newRow.setValue(columns.get(i), rowValues.get(i));
            }
            resultTable.addRow(rowValues.get(primaryKeyIndex), newRow);
        }

        return resultTable;
    }

    public Table update(WhereClause whereClause, SetClause setClause) throws DatabaseError {
        Table resultTable = new Table(this.schema);

        Map<String, Literal> columnValues = setClause.getColumnValues();
        for (Literal key : rows.keySet()) {
            Row row = rows.get(key);
            if (whereClause.satisfiedOnRow(row)) {
                for (String columnName : columnValues.keySet()) {
                    resultTable.validateValue(columnName, columnValues.get(columnName));
                    row.setValue(columnName, columnValues.get(columnName));
                    if (schema.getPrimaryKeyColumn() != null && schema.getPrimaryKeyColumn().equals(columnName)){
                        key = columnValues.get(columnName);
                    }
                }
            }
            resultTable.rows.put(key, row);
        }
        return resultTable;
    }

    public Table delete(WhereClause whereClause) throws DatabaseError {
        Table resultTable = new Table(this.schema);

        for (Literal key : rows.keySet()) {
            Row row = rows.get(key);
            if (! whereClause.satisfiedOnRow(row)) {
                resultTable.rows.put(key, row);
            }
        }
        return resultTable;
    }

    private void addRow(Literal literal, Row row) throws DatabaseError {
        for (String columnName : schema.getColumns().keySet()) {
            try {
                row.getValue(columnName);
            } catch (NullPointerException e) {
                throw new DatabaseError("Row doesn't meet table schema");
            }
        }

        rows.put(literal, row);
    }

    private boolean validateValue(String column, Literal literal) throws DatabaseError {
        if (schema.getColumnDefinition(column).getDataType() != literal.getType()) {
            throw new DatabaseError("One of values in column: " + column + " is of different type");
        }
        if (schema.getColumnDefinition(column).hasConstraint(ColumnDefinition.Constraint.NotNull)
                && literal.getValue() == null) {
            throw new DatabaseError("One of values in column: " + column + " violates constraint: NotNull");
        }
        if (schema.getColumnDefinition(column).hasConstraint(ColumnDefinition.Constraint.PrimaryKey)) {
            String primaryKeyColumn = schema.getPrimaryKeyColumn();
            for (Literal key : rows.keySet()) {
                Row row = rows.get(key);
                if (row.getValue(primaryKeyColumn).getValue().equals(literal.getValue())) {
                    throw new DatabaseError("Duplicate primary key in column: " + column);
                }
            }
        }
        return true;
    }


    public Schema getSchema() {
        return schema;
    }

    public Map<Literal, Row> getRows() {
        return rows;
    }
}
