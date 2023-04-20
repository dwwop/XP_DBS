package core.parsing.tree.statements;

import core.db.table.Table;
import exceptions.DatabaseError;

public class CreateTableStatement extends CreateStatement {

    public CreateTableStatement(String tableName) {
        super(tableName);
    }

    @Override
    public Table execute(Table table) throws DatabaseError {
        return null;
    }
}
