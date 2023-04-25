package core.parsing.tree.clauses.factories;


import core.parsing.tree.clauses.OrderByClause;
import core.parsing.util.KeywordConsumer;
import exceptions.syntaxErrors.EndOfFileError;
import exceptions.syntaxErrors.SyntaxError;

import java.util.*;

public class OrderByFactory extends ClauseFactory {

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
            if (column.endsWith(",")) {
                column = column.substring(0, column.length() - 1);
                columns.add(column);
                orders.add(KeywordConsumer.Keyword.ASC);
                continue;
            }

            String peekedKeyword = tokens.peek();
            if (peekedKeyword == null) {
                columns.add(column);
                orders.add(KeywordConsumer.Keyword.ASC);
                return new OrderByClause(columns, orders);
            }

            boolean isAtEnd = !peekedKeyword.endsWith(",");
            if (peekedKeyword.endsWith(",")) {
                peekedKeyword = peekedKeyword.substring(0, peekedKeyword.length() - 1);
            }

            if (KeywordConsumer.Keyword.ASC.toString().equalsIgnoreCase(peekedKeyword)) {
                tokens.poll();
                columns.add(column);
                orders.add(KeywordConsumer.Keyword.ASC);
            }

            if (KeywordConsumer.Keyword.DESC.toString().equalsIgnoreCase(peekedKeyword)) {
                tokens.poll();
                columns.add(column);
                orders.add(KeywordConsumer.Keyword.DESC);
            }

            if (isAtEnd)
                return new OrderByClause(columns, orders);
        }

    }
}
