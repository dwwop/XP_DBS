package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.ColumnsClause;
import exceptions.SyntaxError;

import java.util.Queue;

public class ColumnsFactory extends ClauseFactory {

    @Override
    public ColumnsClause fromTokens(Queue<String> tokens) throws SyntaxError {
        return null;
    }
}
