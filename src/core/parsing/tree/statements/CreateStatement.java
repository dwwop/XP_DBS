package core.parsing.tree.statements;

public abstract class CreateStatement extends Statement {

    public CreateStatement(String tableName) {
        super(tableName);
    }
}
