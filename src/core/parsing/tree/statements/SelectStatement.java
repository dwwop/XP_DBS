package core.parsing.tree.statements;

import core.db.table.Table;
import core.parsing.tree.clauses.ColumnClause;
import core.parsing.tree.clauses.LimitClause;
import core.parsing.tree.clauses.OrderByClause;
import core.parsing.tree.clauses.WhereClause;
import exceptions.DatabaseError;

import java.util.Objects;

public class SelectStatement extends TableStatement {

    private final ColumnClause columnClause;
    private final WhereClause whereClause;
    private final OrderByClause orderByClause;
    private final LimitClause limitClause;


    public SelectStatement(String tableName, ColumnClause columnClause, WhereClause whereClause, OrderByClause orderByClause, LimitClause limitClause) {
        super(tableName);
        this.columnClause = columnClause;
        this.whereClause = whereClause;
        this.orderByClause = orderByClause;
        this.limitClause = limitClause;
    }

    @Override
    public Table execute(Table table) throws DatabaseError {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectStatement that = (SelectStatement) o;
        return Objects.equals(columnClause, that.columnClause) && Objects.equals(whereClause, that.whereClause) && Objects.equals(orderByClause, that.orderByClause) && Objects.equals(limitClause, that.limitClause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnClause, whereClause, orderByClause, limitClause);
    }

    @Override
    public String toString() {
        return
                "columnClause: " + columnClause + "\n" +
                        "whereClause: " + whereClause + "\n" +
                        "orderByClause: " + orderByClause + "\n" +
                        "limitClause: " + limitClause;
    }
}
