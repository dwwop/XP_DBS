package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.Clause;
import exceptions.syntax.SyntaxError;

import java.util.Queue;

public abstract class ClauseFactory {

    public abstract Clause fromTokens(Queue<String> tokens) throws SyntaxError;
}
