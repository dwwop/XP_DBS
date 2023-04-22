package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.SelectClause;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class SelectCFactory extends ClauseFactory {
    private static final String allToken = "*";

    private static void throwSyntaxError() throws SyntaxError {
        throw new EndOfFileError("column_name");
    }

    @Override
    public SelectClause fromTokens(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty()) {
            throwSyntaxError();
        }
        if (Objects.equals(tokens.peek(), allToken)) {
            tokens.poll();
            return new SelectClause(true);
        }

        List<String> columns = new ArrayList<>();
        while (true) {
            if (tokens.isEmpty()) {
                throwSyntaxError();
            }

            String column = tokens.poll();
            if (column.equals(","))
                throw new TokenError(",", "column_name");
            columns.add(column);

            if (tokens.peek() == null || !tokens.peek().equals(",")) {
                break;
            }
            tokens.poll();
        }

        return new SelectClause(columns);
    }
}
