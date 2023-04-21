package core.parsing.tree.statements;

import core.db.table.Table;
import exceptions.DatabaseError;

public class SelectStatement extends TableStatement {

    public SelectStatement(String tableName) {
        super(tableName);
    }

    @Override
    public Table execute(Table table) throws DatabaseError {
        return null;
    }
}
