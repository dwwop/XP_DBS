package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.ValuesClause;
import exceptions.SyntaxError;

import java.util.Queue;

public class ValuesFactory extends ClauseFactory {

    @Override
    public ValuesClause fromTokens(Queue<String> tokens) throws SyntaxError {
        return null;
    }
}
