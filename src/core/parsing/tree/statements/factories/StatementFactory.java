package core.parsing.tree.statements.factories;

import core.parsing.tree.statements.Statement;
import exceptions.SyntaxError;

import java.util.Queue;

public abstract class StatementFactory {

    public abstract Statement fromTokens(Queue<String> tokens) throws SyntaxError;
}
