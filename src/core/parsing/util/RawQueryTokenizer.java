package core.parsing.util;

import exceptions.SyntaxError;
import util.Strings;

import java.util.*;

public class RawQueryTokenizer {

    public enum TupleType {
        Schema, ColumnsList, ValueTuple
    }

    private static final Map<TupleType, String> tupleTypeDisplayNames = Map.of(
        TupleType.Schema, "schema",
        TupleType.ColumnsList, "list of columns",
        TupleType.ValueTuple, "value tuple"
    );

    public static Queue<String> tokenizeQuery(String rawQuery) {
        if (rawQuery.isEmpty()) {
            return new LinkedList<>();
        }

        return new LinkedList<>(Arrays.asList(rawQuery.split(" ")));
    }

    public static Queue<String> tokenizeTupleTrimmingOrFail(TupleType tupleType, String rawTuple) throws SyntaxError {
        String trimmedRawTuple = rawTuple.trim();

        if (!trimmedRawTuple.matches("^\\(.*\\)$")) {
            throw new SyntaxError("Ill-formed " + tupleTypeDisplayNames.get(tupleType) + ": '" + rawTuple + "'.");
        }

        List<String> elements = Strings.splitAndTrimOnTopLevel(
            trimmedRawTuple.substring(1, trimmedRawTuple.length() - 1), ','
        );

        return new LinkedList<>(elements);
    }

    public static void consumeEmptyTokens(Queue<String> tokens) {
        while (!tokens.isEmpty() && tokens.peek().isEmpty()) {
            tokens.poll();
        }
    }
}
