package core.parsing.tree.statements;

import core.Result;
import core.db.TableManager;
import core.db.table.Schema;
import core.parsing.tree.clauses.WhereClause;
import exceptions.DatabaseError;

public class CreateTableStatement extends Statement {

    private final Schema schema;

    public CreateTableStatement(Schema schema) {
        this.schema = schema;
    }

    @Override
    public Result execute(TableManager tableManager) {
        try {
            tableManager.createTable(schema);

            return new Result(true, "Table created successfully.", null);
        } catch (DatabaseError error) {
            return new Result(false, error.getMessage(), null);
        }
    }
}
