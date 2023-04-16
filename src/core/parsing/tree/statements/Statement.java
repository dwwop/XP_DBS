package core.parsing.tree.statements;

import core.commands.Command;

public abstract class Statement {

    public abstract Command toCommand();
}
