package core.parsing.tree.clauses.factories;

import core.db.types.Literal;
import core.parsing.IdentifierExtractor;
import core.parsing.LiteralExtractor;
import core.parsing.tree.clauses.ValuesClause;
import exceptions.SyntaxError;
import util.Strings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ValuesFactory extends ClauseFactory {

    @Override
    public ValuesClause fromTokens(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty()) {
            throw new SyntaxError("The end of the query was reached but a list of values was expected.");
        }

        String rawValuesClause = getRawValuesClause(tokens);

        List<String> valuesList = Strings.splitAndTrimOnTopLevel(rawValuesClause, ',');
        List<List<Literal>> tuples = new ArrayList<>();

        for (String rawTuple : valuesList) {
            List<Literal> tuple = parseTuple(rawTuple);

            int expectedTupleLength = tuples.isEmpty() ? tuple.size() : tuples.get(0).size();

            if (tuple.size() != expectedTupleLength) {
                throw new SyntaxError("All value tuples must be of same length.");
            }

            tuples.add(parseTuple(rawTuple));
        }

        return new ValuesClause(tuples);
    }

    private String getRawValuesClause(Queue<String> tokens) {
        StringBuilder rawValuesClause = new StringBuilder();

        while (!tokens.isEmpty()) {
            rawValuesClause.append(" ").append(tokens.poll());
        }

        return rawValuesClause.toString();
    }

    private List<Literal> parseTuple(String rawTuple) throws SyntaxError {
        if (!rawTuple.matches("^\\(.*\\)$")) {
            throw new SyntaxError("Ill-formed tuple: '" + rawTuple + "'.");
        }

        Queue<String> tokens = new LinkedList<>(
            Strings.splitAndTrimOnTopLevel(rawTuple.substring(1, rawTuple.length() - 1), ',')
        );
        List<Literal> tuple = new ArrayList<>();

        while (!tokens.isEmpty()) {
            tuple.add(LiteralExtractor.pollLiteralOrFail(tokens));
        }

        return tuple;
    }
}
