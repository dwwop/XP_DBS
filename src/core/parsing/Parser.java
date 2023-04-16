package core.parsing;

import core.parsing.tree.statements.SelectStatement;
import core.parsing.tree.statements.Statement;

public class Parser {

    public Statement parse(String query) {
        return new SelectStatement();
    }
}
