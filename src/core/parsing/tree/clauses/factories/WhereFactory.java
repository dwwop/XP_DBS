package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.WhereClause;
import exceptions.syntaxErrors.SyntaxError;

import java.util.Queue;

public class WhereFactory extends ClauseFactory {

    public WhereClause getEmptyClause() {
        return new WhereClause();
    }

    @Override
    public WhereClause fromTokens(Queue<String> tokens) throws SyntaxError {
        return null;
    }
}
