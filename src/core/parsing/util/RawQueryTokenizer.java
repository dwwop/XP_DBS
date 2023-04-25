package core.parsing.util;


import exceptions.syntax.SyntaxError;
import util.Strings;

import java.util.*;

public class RawQueryTokenizer {

    private static final Map<TupleType, String> tupleTypeDisplayNames = Map.of(
            TupleType.Schema, "schema",
            TupleType.ColumnsList, "list of columns",
            TupleType.ValueTuple, "value tuple"
    );

    private static final Set<Character> specialChars = Set.of('(', ')', ',');

    private static String extractComparator(String query, Integer index) {
        String curChar = String.valueOf(query.charAt(index));
        if (index + 1 < query.length()) {
            String token = curChar + query.charAt(index + 1);
            if (ComparatorConsumer.isComparator(token)) {
                return token;
            }
        }
        if (ComparatorConsumer.isComparator(curChar)) {
            return curChar;
        }
        return null;
    }

    private static String handleSpecialCharacters(String query) throws SyntaxError {
        boolean qouteStarted = false;
        StringBuilder newQuery = new StringBuilder();
        for (int i = 0; i < query.length(); i++) {
            Character ch = query.charAt(i);
            if (ch == '\u200b')
                throw new SyntaxError("Unsupported zero width space detected.");

            if (ch == '\"' && qouteStarted) {
                qouteStarted = false;
            } else if (ch == '\"') {
                qouteStarted = true;
            }

            if (qouteStarted) {
                newQuery.append(ch);
                continue;
            }

            String comparator = extractComparator(query, i);
            if (comparator != null) {
                if (comparator.length() == 2)
                    i++;
                newQuery.append("\u200b").append(comparator).append("\u200b");
                continue;
            }

            if (specialChars.contains(ch))
                newQuery.append("\u200b").append(ch).append("\u200b");
            else if (Character.isWhitespace(ch))
                newQuery.append('\u200b');
            else
                newQuery.append(ch);

        }
        if (qouteStarted)
            throw new SyntaxError("Unclosed string bracket detected");
        return newQuery.toString();
    }

    public static Queue<String> tokenizeQuery(String rawQuery) throws SyntaxError {
        if (rawQuery.isEmpty()) {
            return new LinkedList<>();
        }
        rawQuery = handleSpecialCharacters(rawQuery);
        return new LinkedList<>(Arrays.asList(rawQuery.split("\u200b+")));
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

    public enum TupleType {
        Schema, ColumnsList, ValueTuple
    }
}
