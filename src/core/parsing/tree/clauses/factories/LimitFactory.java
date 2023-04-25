package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.LimitClause;
import core.parsing.util.KeywordConsumer;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;

import java.util.Queue;

public class LimitFactory extends ClauseFactory {

    private Integer getInteger(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty())
            throw new EndOfFileError("number");

        String token = tokens.poll();

        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException ex) {
            throw new TokenError(token, "number");
        }
    }

    @Override
    public LimitClause fromTokens(Queue<String> tokens) throws SyntaxError {
        Integer numberRows = getInteger(tokens);

        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.OFFSET, tokens)) {
            Integer offsetRows = getInteger(tokens);
            return new LimitClause(numberRows, offsetRows);
        }

        return new LimitClause(numberRows, 0);
    }

}
