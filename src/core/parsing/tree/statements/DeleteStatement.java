package core.parsing.tree.statements;

import core.db.table.Table;
import core.parsing.tree.clauses.WhereClause;
import exceptions.DatabaseError;

public class DeleteStatement extends TableStatement {

    private final WhereClause whereClause;

    public DeleteStatement(String tableName, WhereClause whereClause) {
        super(tableName);

        this.whereClause = whereClause;
    }

    public WhereClause getWhereClause() {
        return whereClause;
    }

    @Override
    public Table execute(Table table) throws DatabaseError {
        return table.delete(whereClause);
    }
}
