package core.parsing.tree.statements;

import core.Result;
import core.db.TableManager;
import core.db.table.Schema;
import exceptions.DatabaseError;

public class CreateTableStatement extends Statement {

    private final String tableName;
    private final Schema schema;

    public CreateTableStatement(String tableName, Schema schema) {
        this.tableName = tableName;
        this.schema = schema;
    }

    public String getTableName() {
        return tableName;
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public Result execute(TableManager tableManager) {
        try {
            tableManager.createTable(tableName, schema);

            return new Result(true, "Table created successfully.", null);
        } catch (DatabaseError error) {
            return new Result(false, error.getMessage(), null);
        }
    }
}
