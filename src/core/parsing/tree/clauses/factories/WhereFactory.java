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


    private Condition consumeCondition(String headToken, Queue<String> tokens, Condition condition) throws SyntaxError {
        if (headToken == null || KeywordConsumer.isStatementKeyword(headToken)) {
            if (condition != null)
                return condition;
            if (headToken == null)
                throw new EndOfFileError("condition");
            if (KeywordConsumer.isStatementKeyword(headToken)) {
                throw new TokenError(tokens.peek(), "condition");
            }
        }

        if (headToken.equals("(")) {
            Condition con = consumeCondition(tokens.poll(), tokens, condition);
            return consumeCondition(tokens.poll(), tokens, con);
        }
        if (headToken.equals(")")) {
            if (condition == null)
                throw new TokenError(")", "condition");
            return condition;
        }

        if (KeywordConsumer.Keyword.NOT.toString().equals(headToken))
            return new NotCondition(consumeCondition(tokens.poll(), tokens, null));
        if (KeywordConsumer.Keyword.AND.toString().equals(headToken))
            return new AndCondition(condition, consumeCondition(tokens.poll(), tokens, null));
        if (KeywordConsumer.Keyword.OR.toString().equals(headToken))
            return new OrCondition(condition, consumeCondition(tokens.poll(), tokens, null));

        if (headToken.startsWith("(")) {
            Condition con = consumeCondition(headToken.substring(1), tokens, condition);
            return consumeCondition(tokens.poll(), tokens, con);
        }

        if (headToken.endsWith(")")) {
            if (condition == null)
                throw new TokenError(")", "condition");
            headToken = headToken.substring(0, headToken.length() - 1);

        }

        String comparator = ComparatorConsumer.consumeComparatorOrFail(tokens);

        if (tokens.isEmpty())
            throw new EndOfFileError("value");

        String value = tokens.poll();

        if (value.startsWith("'") && value.endsWith("'"))
            value = value.substring(1, value.length() - 1);

        return consumeCondition(tokens.poll(), tokens, new Expression(headToken, comparator, value));
    }

    @Override
    public WhereClause fromTokens(Queue<String> tokens) throws SyntaxError {
        return new WhereClause(consumeCondition(tokens.poll(), tokens, null));
    }
}
