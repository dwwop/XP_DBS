package core.parsing.tree.statements;

import core.db.table.Table;
import core.parsing.tree.clauses.SetClause;
import core.parsing.tree.clauses.WhereClause;
import exceptions.DatabaseError;

public class UpdateStatement extends TableStatement {

    private final WhereClause whereClause;
    private final SetClause setClause;

    public UpdateStatement(String tableName, WhereClause whereClause, SetClause setClause) {
        super(tableName);

        this.whereClause = whereClause;
        this.setClause = setClause;
    }

    public WhereClause getWhereClause() {
        return whereClause;
    }

    public SetClause getSetClause() {
        return setClause;
    }

    @Override
    public Table execute(Table table) throws DatabaseError {
        return table.update(whereClause, setClause);
    }
}
