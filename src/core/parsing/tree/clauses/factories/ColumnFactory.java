package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.ColumnClause;
import exceptions.syntaxErrors.EndOfFileError;
import exceptions.syntaxErrors.SyntaxError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class ColumnFactory extends ClauseFactory {
    private static final String allToken = "*";

    private static void throwSyntaxError() throws SyntaxError {
        throw new EndOfFileError("column_name");
    }

    @Override
    public ColumnClause fromTokens(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty()) {
            throwSyntaxError();
        }
        if (Objects.equals(tokens.peek(), allToken)) {
            tokens.poll();
            return new ColumnClause(true);
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

        return new ColumnClause(columns);
    }
}
