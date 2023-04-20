package core.parsing.tree.statements;

import core.db.table.Table;
import exceptions.DatabaseError;

public class InsertStatement extends Statement {

    public InsertStatement(String tableName) {
        super(tableName);
    }

    @Override
    public Table execute(Table table) throws DatabaseError {
        return null;
    }
}
