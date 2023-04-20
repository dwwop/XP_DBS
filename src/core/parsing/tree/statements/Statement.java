package core.parsing.tree.statements;

import core.Result;
import core.db.TableManager;

public abstract class Statement {

    public abstract Result execute(TableManager tableManager);
}
