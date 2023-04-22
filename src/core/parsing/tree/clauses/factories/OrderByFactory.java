package core.parsing.tree.clauses.factories;


import core.parsing.tree.clauses.OrderByClause;
import core.parsing.util.KeywordConsumer;
import exceptions.syntaxErrors.EndOfFileError;
import exceptions.syntaxErrors.SyntaxError;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class OrderByFactory extends ClauseFactory {

    @Override
    public OrderByClause fromTokens(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty())
            throw new EndOfFileError("column_name");

        Map<String, KeywordConsumer.Keyword> columnsAndOrders = new HashMap<>();
        while (true) {
            if (tokens.isEmpty())
                throw new EndOfFileError("column_name");
            String column = tokens.poll();
            if (column.endsWith(",")) {
                column = column.substring(0, column.length() - 1);
                columnsAndOrders.put(column, KeywordConsumer.Keyword.ASC);
                continue;
            }

            String peekedKeyword = tokens.peek();
            if (peekedKeyword == null) {
                columnsAndOrders.put(column, KeywordConsumer.Keyword.ASC);
                return new OrderByClause(columnsAndOrders);
            }

            boolean isAtEnd = !peekedKeyword.endsWith(",");
            if (peekedKeyword.endsWith(",")) {
                peekedKeyword = peekedKeyword.substring(0, peekedKeyword.length() - 1);
            }

            if (KeywordConsumer.Keyword.ASC.toString().equalsIgnoreCase(peekedKeyword)) {
                tokens.poll();
                columnsAndOrders.put(column, KeywordConsumer.Keyword.ASC);
            }

            if (KeywordConsumer.Keyword.DESC.toString().equalsIgnoreCase(peekedKeyword)) {
                tokens.poll();
                columnsAndOrders.put(column, KeywordConsumer.Keyword.DESC);
            }

            if (isAtEnd)
                return new OrderByClause(columnsAndOrders);
        }

    }
}
