package core.parsing.tree.clauses.factories;

import core.parsing.KeywordConsumer;
import core.parsing.tree.clauses.WhereClause;
import core.parsing.tree.clauses.conditions.*;
import exceptions.syntaxErrors.EndOfFileError;
import exceptions.syntaxErrors.SyntaxError;
import exceptions.syntaxErrors.TokenError;

import java.util.Queue;

public class WhereFactory extends ClauseFactory {

    public WhereClause getEmptyClause() {
        return null;
    }


    private boolean consumeBracket(String bracket, Queue<String> tokens) {
        if (tokens.isEmpty() || !bracket.equals(tokens.peek())) {
            return false;
        }

        tokens.poll();

        return true;
    }



    private Condition consumeCondition(Queue<String> tokens, Condition condition) throws SyntaxError {
        if (tokens.isEmpty() || KeywordConsumer.isStatementKeyword(tokens)) {
            if (condition != null)
                return condition;
            if (tokens.isEmpty())
                throw new EndOfFileError("condition");
            if (KeywordConsumer.isStatementKeyword(tokens)) {
                throw new TokenError(tokens.peek(), "condition");
            }
        }

        if (consumeBracket("(", tokens))
            return consumeCondition(tokens, consumeCondition(tokens, condition));
        if (consumeBracket(")", tokens)) {
            if (condition == null)
                throw new TokenError(")", "condition");
            return condition;
        }

        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.NOT, tokens))
            return new NotCondition(consumeCondition(tokens, null));
        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.AND, tokens))
            return new AndCondition(condition, consumeCondition(tokens, null));
        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.OR, tokens))
            return new OrCondition(condition, consumeCondition(tokens, null));

        String columnName = tokens.poll();

        String comparator = ComparatorConsumer.consumeComparatorOrFail(tokens);

        if (tokens.isEmpty())
            throw new EndOfFileError("value");

        String value = tokens.poll();

        if (value.startsWith("'") && value.endsWith("'"))
            value = value.substring(1, value.length() - 1);

        return consumeCondition(tokens, new Expression(columnName, comparator, value));
    }

    @Override
    public WhereClause fromTokens(Queue<String> tokens) throws SyntaxError {
        return new WhereClause(consumeCondition(tokens, null));
    }
}
