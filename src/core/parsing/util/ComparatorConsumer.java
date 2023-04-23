package core.parsing.util;

import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;

import java.util.Queue;
import java.util.Set;

public class ComparatorConsumer {
    public static final Set<String> comparators = Set.of(
            "=",
            "!=",
            ">=",
            "<=",
            ">",
            "<"
    );

    public static boolean isComparator(String token) {
        return comparators.contains(token);
    }

    public static String consumeComparatorOrFail(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty()) {
            throw new EndOfFileError("comparator");
        }

        String token = tokens.poll();

        if (!isComparator(token)) {
            throw new TokenError(token, "comparator");
        }

        return token;
    }
}
