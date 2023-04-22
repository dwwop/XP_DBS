package core.parsing.tree.statements;

import core.Result;
import core.db.TableManager;
import core.db.table.Table;
import exceptions.DatabaseError;

public abstract class TableStatement extends Statement {

    private final String tableName;

    public TableStatement(String tableName) {
        this.tableName = tableName;
    }

    public Result execute(TableManager tableManager) {
        try {
            Table table = tableManager.getTable(tableName);
            Table result = execute(table);

            return new Result(true, "Statement executed successfully.", result);
        } catch (DatabaseError error) {
            return new Result(false, error.getMessage(), null);
        }
    }

    protected abstract Table execute(Table table) throws DatabaseError;
}
