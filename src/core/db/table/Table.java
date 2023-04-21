package core.db.table;

import core.db.types.Literal;
import core.parsing.tree.clauses.*;

import java.util.Map;
import java.util.TreeMap;

public class Table {

    private Schema schema;

    private Map<Literal, Row> rows = new TreeMap<>();

    public Table(Schema schema) {
        this.schema = schema;
    }

    public Table select(SelectClause selectClause) {
        return new Table(new Schema());
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

    public Table offset(OffsetClause offsetClause) {
        return new Table(new Schema());
    }

    public Table insert(ColumnsClause columnsClause, ValuesClause valuesClause) {
        return null;
    }

    public Table update(WhereClause whereClause, SetClause setClause) {
        return null;
    }

    public Table delete(WhereClause whereClause) {
        return null;
    }
}
