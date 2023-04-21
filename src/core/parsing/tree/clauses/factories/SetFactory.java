package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.SetClause;
import exceptions.syntaxErrors.SyntaxError;

import java.util.Queue;

public class SetFactory extends ClauseFactory {

    @Override
    public SetClause fromTokens(Queue<String> tokens) throws SyntaxError {
        return null;
    }
}
