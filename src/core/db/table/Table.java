package core.db.table;

import core.db.types.Literal;
import core.parsing.tree.clauses.*;
import exceptions.DatabaseError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Table {

    private final Schema schema;

    private final Map<Literal, Row> rows = new TreeMap<>();

    public Table(Schema schema) {
        this.schema = schema;
    }

    public Table select(SelectClause selectClause) {
        if (selectClause.getAllColumns()){
            return this;
        }

        Schema resultTableSchema = new Schema();
        try {
            for (String columnName : selectClause.getColumnNames()){
                resultTableSchema.setColumnDefinition(columnName, schema.getColumnDefinition(columnName));
            }
            if (resultTableSchema.hasColumn(schema.getPrimaryKeyColumn())){
                resultTableSchema.setPrimaryKeyColumn(schema.getPrimaryKeyColumn());
            }
        } catch (DatabaseError e){
            return new Table(new Schema());
        }

        Table resultTable = new Table(resultTableSchema);

        for (Literal literal : rows.keySet()){
            Row row = rows.get(literal);
            Row newRow = new Row(new HashMap<>());
            for (String column : selectClause.getColumnNames()){
                newRow.setValue(column, row.getValue(column));
            }
            resultTable.addRow(literal, newRow);
        }

        return resultTable;
    }

    public Table where(WhereClause whereClause) {
        return new Table(new Schema());
    }

    public Table orderBy(OrderByClause orderByClause) {
        return new Table(new Schema());
    }

    public Table limit(LimitClause limitClause) {
        return new Table(new Schema());
    }

    public Table insert(ColumnsClause columnsClause, ValuesClause valuesClause) {
        List<String> columns = columnsClause.getColumns();
        int primaryKeyIndex = columns.indexOf(schema.getPrimaryKeyColumn());
        if (primaryKeyIndex == -1){
            //TODO vymysliet co robit ked nemoze insertnut (asi exception?)
            return new Table(new Schema());
        }

        for (List<Literal> rowValues : valuesClause.getValues()){
            Row newRow = new Row(new HashMap<>());
            for (int i = 0; i < rowValues.size(); i++){
                newRow.setValue(columns.get(i), rowValues.get(i));
            }
            addRow(rowValues.get(primaryKeyIndex), newRow);
        }

        return this;
    }

    public Table update(WhereClause whereClause, SetClause setClause) {
        return null;
    }

    public Table delete(WhereClause whereClause) {
        return null;
    }

    private void addRow(Literal literal, Row row){
        //TODO check the row being added
        rows.put(literal, row);
    }
}
