package core.parsing.tree.clauses.factories;


import core.parsing.tree.clauses.OrderByClause;
import core.parsing.util.KeywordConsumer;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class OrderByFactory extends ClauseFactory {

    private boolean consumeComma(Queue<String> tokens) {
        if (tokens.peek() == null || !tokens.peek().equals(","))
            return false;

        tokens.poll();
        return true;
    }

    @Override
    public OrderByClause fromTokens(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty())
            throw new EndOfFileError("column_name");

        List<String> columns = new ArrayList<>();
        List<KeywordConsumer.Keyword> orders = new ArrayList<>();
        while (true) {
            if (tokens.isEmpty())
                throw new EndOfFileError("column_name");
            String column = tokens.poll();
            if (column.equals(","))
                throw new TokenError(",", "column_name");

            if (consumeComma(tokens)) {
                columns.add(column);
                orders.add(KeywordConsumer.Keyword.ASC);
                continue;
            }

            if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.ASC, tokens)) {
                columns.add(column);
                orders.add(KeywordConsumer.Keyword.ASC);
                if (consumeComma(tokens))
                    continue;
                return new OrderByClause(columns, orders);
            }

            if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.DESC, tokens)) {
                columns.add(column);
                orders.add(KeywordConsumer.Keyword.DESC);
                if (consumeComma(tokens))
                    continue;
                return new OrderByClause(columns, orders);
            }
            columns.add(column);
            orders.add(KeywordConsumer.Keyword.ASC);
            return new OrderByClause(columns, orders);
        }

    }
}
