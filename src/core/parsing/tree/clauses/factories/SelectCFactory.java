package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.SelectClause;
import exceptions.syntaxErrors.EndOfFileError;
import exceptions.syntaxErrors.SyntaxError;

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
        while (tokens.peek().endsWith(",")) {
            String column = tokens.poll();
            column = column.substring(0, column.length() - 1);

            columns.add(column);
            if (tokens.isEmpty())
                throwSyntaxError();
        }

        String column = tokens.poll();
        columns.add(column);

        return new SelectClause(columns);
    }
}
