package core.parsing.tree.statements;

import core.db.table.Table;
import core.parsing.tree.clauses.SelectClause;
import core.parsing.tree.clauses.LimitClause;
import core.parsing.tree.clauses.OrderByClause;
import core.parsing.tree.clauses.WhereClause;
import exceptions.DatabaseError;

import java.util.Objects;

public class SelectStatement extends TableStatement {

    private final SelectClause selectClause;
    private final WhereClause whereClause;
    private final OrderByClause orderByClause;
    private final LimitClause limitClause;


    public SelectStatement(String tableName, SelectClause selectClause, WhereClause whereClause, OrderByClause orderByClause, LimitClause limitClause) {
        super(tableName);
        this.selectClause = selectClause;
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
        return Objects.equals(selectClause, that.selectClause) && Objects.equals(whereClause, that.whereClause) && Objects.equals(orderByClause, that.orderByClause) && Objects.equals(limitClause, that.limitClause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selectClause, whereClause, orderByClause, limitClause);
    }

    @Override
    public String toString() {
        return
                "columnClause: " + selectClause + "\n" +
                        "whereClause: " + whereClause + "\n" +
                        "orderByClause: " + orderByClause + "\n" +
                        "limitClause: " + limitClause;
    }
}
