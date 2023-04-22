package core.parsing.tree.clauses.factories;

import core.db.types.Literal;
import core.parsing.tree.clauses.ValuesClause;
import core.parsing.util.LiteralExtractor;
import core.parsing.util.RawQueryBuilder;
import core.parsing.util.RawQueryTokenizer;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ValuesFactory extends ClauseFactory {

    @Override
    public ValuesClause fromTokens(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty()) {
            throw new EndOfFileError("list of values");
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
        RawQueryBuilder rawValuesClause = new RawQueryBuilder();

        while (!tokens.isEmpty()) {
            rawValuesClause.append(tokens.poll());
        }

        return rawValuesClause.build();
    }

    private List<Literal> parseTuple(String rawTuple) throws SyntaxError {
        Queue<String> values = RawQueryTokenizer.tokenizeTupleTrimmingOrFail(
                RawQueryTokenizer.TupleType.ColumnsList, rawTuple
        );
        List<Literal> tuple = new ArrayList<>();

        while (!values.isEmpty()) {
            tuple.add(LiteralExtractor.pollLiteralOrFail(values));
        }

        return tuple;
    }
}
