package core.parsing.tree.statements;

import core.db.table.Table;
import core.parsing.tree.clauses.ColumnsClause;
import core.parsing.tree.clauses.ValuesClause;
import exceptions.DatabaseError;

public class InsertStatement extends TableStatement {

    private final ColumnsClause columnsClause;
    private final ValuesClause valuesClause;

    public InsertStatement(String tableName, ColumnsClause columnsClause, ValuesClause valuesClause) {
        super(tableName);

        this.columnsClause = columnsClause;
        this.valuesClause = valuesClause;
    }

    @Override
    public Table execute(Table table) throws DatabaseError {
        return table.insert(columnsClause, valuesClause);
    }
}
