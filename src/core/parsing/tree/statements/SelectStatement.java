package core.parsing.tree.statements;

import core.db.table.Table;
import core.parsing.tree.clauses.LimitClause;
import core.parsing.tree.clauses.OrderByClause;
import core.parsing.tree.clauses.SelectClause;
import core.parsing.tree.clauses.WhereClause;
import exceptions.DatabaseError;

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
        if (selectClause != null){
            table = table.select(selectClause);
        }
        if (whereClause != null){
            table = table.where(whereClause);
        }
        if (orderByClause != null){
            table = table.orderBy(orderByClause);
        }
        if (limitClause != null){
            table = table.limit(limitClause);
        }
        return table;
    }

    public SelectClause getSelectClause() {
        return selectClause;
    }

    public WhereClause getWhereClause() {
        return whereClause;
    }

    public OrderByClause getOrderByClause() {
        return orderByClause;
    }

    public LimitClause getLimitClause() {
        return limitClause;
    }
}
