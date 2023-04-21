package core.parsing.tree.clauses.factories;

import core.parsing.IdentifierExtractor;
import core.parsing.tree.clauses.ColumnsClause;
import exceptions.SyntaxError;
import util.Strings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ColumnsFactory extends ClauseFactory {

    @Override
    public ColumnsClause fromTokens(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty()) {
            throw new SyntaxError("The end of the query was reached but a columns list was expected.");
        }

        String rawColumnsClause = getRawColumnsClause(tokens);

        if (!rawColumnsClause.matches("^\\(.*\\)$")) {
            throw new SyntaxError("Ill-formed columns list: '" + rawColumnsClause + "'.");
        }

        List<String> columnsList = Strings.splitAndTrim(
            rawColumnsClause.substring(1, rawColumnsClause.length() - 1), ","
        );

        List<String> columnNames = parseColumnsList(columnsList);

        return new ColumnsClause(columnNames);
    }

    private String getRawColumnsClause(Queue<String> tokens) {
        StringBuilder rawColumnsClause = new StringBuilder();

        while (!tokens.isEmpty()) {
            String part = tokens.poll();

            rawColumnsClause.append(" ").append(part);

            if (part.endsWith(")")) {
                break;
            }
        }

        return rawColumnsClause.toString();
    }

    private List<String> parseColumnsList(List<String> columnsList) throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(columnsList);
        List<String> columnNames = new ArrayList<>();

        while (!tokens.isEmpty()) {
            columnNames.add(IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.ColumnName, tokens));
        }

        return columnNames;
    }
}
