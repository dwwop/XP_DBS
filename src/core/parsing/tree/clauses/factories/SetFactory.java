package core.parsing.tree.clauses.factories;

import core.db.types.Literal;
import core.parsing.tree.clauses.SetClause;
import core.parsing.util.IdentifierExtractor;
import core.parsing.util.KeywordConsumer;
import core.parsing.util.LiteralExtractor;
import core.parsing.util.RawQueryBuilder;
import exceptions.syntax.SyntaxError;
import util.Strings;

import java.util.*;

public class SetFactory extends ClauseFactory {

    private Map<String, Literal> columnValues;

    @Override
    public SetClause fromTokens(Queue<String> tokens) throws SyntaxError {
        columnValues = new HashMap<>();

        String rawSetClause = getRawSetClause(tokens);

        List<String> columnValueAssignements = Strings.splitAndTrimOnTopLevel(rawSetClause, ',');

        if (columnValueAssignements.isEmpty()) {
            throw new SyntaxError("A list of column value assignments is missing.");
        }

        for (String rawAssignement : columnValueAssignements) {
            parseColumnValueAssignment(rawAssignement);
        }

        return new SetClause(columnValues);
    }

    private String getRawSetClause(Queue<String> tokens) {
        RawQueryBuilder rawSetClause = new RawQueryBuilder();
        boolean stringLiteralStarted = false;

        while (!tokens.isEmpty() && !isStartOfWhereClause(tokens, stringLiteralStarted)) {
            String part = tokens.poll();

            if (part.chars().filter(ch -> ch == '"').count() % 2 == 1) {
                stringLiteralStarted = !stringLiteralStarted;
            }

            rawSetClause.append(part);
        }

        return rawSetClause.build();
    }

    private boolean isStartOfWhereClause(Queue<String> tokens, boolean stringLiteralStarted) {
        return KeywordConsumer.isKeyword(KeywordConsumer.Keyword.WHERE, tokens.peek()) && !stringLiteralStarted;
    }

    private void parseColumnValueAssignment(String rawAssignement) throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(Strings.splitAndTrimOnTopLevel(rawAssignement, '='));

        if (tokens.size() != 2) {
            throw new SyntaxError("Invalid column value assignment: '" + rawAssignement + "'.");
        }

        String columnName = IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.ColumnName, tokens);

        if (columnValues.containsKey(columnName)) {
            throw new SyntaxError("Multiple values assigned to the column '" + columnName + "'.");
        }

        Literal columnValue = LiteralExtractor.pollLiteralOrFail(tokens);

        columnValues.put(columnName, columnValue);
    }
}
