package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.WhereClause;
import core.parsing.tree.clauses.conditions.*;
import core.parsing.util.ComparatorConsumer;
import core.parsing.util.KeywordConsumer;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;

import java.util.Queue;

public class WhereFactory extends ClauseFactory {

    public WhereClause getEmptyClause(){
        return new WhereClause();
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

        if (")".equals(tokens.peek())) {
            return condition;
        }

        if (consumeBracket("(", tokens)) {
            if (")".equals(tokens.peek()))
                throw new TokenError(tokens.peek(), "condition");
            Condition returnedCondition = consumeCondition(tokens, condition);
            if (consumeBracket(")", tokens))
                return consumeCondition(tokens, returnedCondition);
            throw new TokenError(tokens.poll(), ")");
        }

        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.NOT, tokens))
            return new NotCondition(consumeCondition(tokens, null));
        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.AND, tokens))
            return new AndCondition(condition, consumeCondition(tokens, null));
        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.OR, tokens))
            return new OrCondition(condition, consumeCondition(tokens, null));

        String columnName = tokens.poll();
        if (ComparatorConsumer.isComparator(columnName))
            throw new TokenError(columnName, "column_name");

        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.IS, tokens)) {
            if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.NOT, tokens) &&
                    KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.NULL, tokens))
                return consumeCondition(tokens, new NotCondition(new NullCondition(columnName)));
            if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.NULL, tokens))
                return consumeCondition(tokens, new NullCondition(columnName));

            throw new TokenError(tokens.peek(), "'NOT NULL' or 'NULL'");
        }

        String comparator = ComparatorConsumer.consumeComparatorOrFail(tokens);

        if (tokens.isEmpty())
            throw new EndOfFileError("value");

        String value = tokens.poll();
        if (ComparatorConsumer.isComparator(value))
            throw new TokenError(value, "value");

        if (value.startsWith("\"") && value.endsWith("\""))
            value = value.substring(1, value.length() - 1);
        else if (value.startsWith("\"") || value.endsWith("\""))
            throw new SyntaxError("Unclosed string found.");

        return consumeCondition(tokens, new Expression(columnName, comparator, value));
    }

    @Override
    public WhereClause fromTokens(Queue<String> tokens) throws SyntaxError {
        return new WhereClause(consumeCondition(tokens, null));
    }
}
