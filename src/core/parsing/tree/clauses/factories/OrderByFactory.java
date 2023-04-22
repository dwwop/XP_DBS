package core.parsing.tree.clauses.factories;


import core.parsing.tree.clauses.OrderByClause;
import core.parsing.util.KeywordConsumer;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class OrderByFactory extends ClauseFactory {

    private boolean consumeComma(Queue<String> tokens){
        if (tokens.peek() == null || !tokens.peek().equals(","))
            return false;

        tokens.poll();
        return true;
    }

    @Override
    public OrderByClause fromTokens(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty())
            throw new EndOfFileError("column_name");

        Map<String, KeywordConsumer.Keyword> columnsAndOrders = new HashMap<>();
        while (true) {
            if (tokens.isEmpty())
                throw new EndOfFileError("column_name");
            String column = tokens.poll();
            if (column.equals(","))
                throw new TokenError(",", "column_name");

            if (consumeComma(tokens)){
                columnsAndOrders.put(column, KeywordConsumer.Keyword.ASC);
                continue;
            }

            if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.ASC, tokens)){
                columnsAndOrders.put(column, KeywordConsumer.Keyword.ASC);
                if (consumeComma(tokens))
                    continue;
                return new OrderByClause(columnsAndOrders);
            }

            if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.DESC, tokens)){
                columnsAndOrders.put(column, KeywordConsumer.Keyword.DESC);
                if (consumeComma(tokens))
                    continue;
                return new OrderByClause(columnsAndOrders);
            }

            columnsAndOrders.put(column, KeywordConsumer.Keyword.ASC);
            return new OrderByClause(columnsAndOrders);
        }

    }
}
