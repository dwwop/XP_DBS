package core.parsing.tree.statements;

import core.Result;
import core.db.TableManager;

public class CreateTableStatement extends CreateStatement {

    @Override
    public Result execute(TableManager tableManager) {
        return null;
    }
}
