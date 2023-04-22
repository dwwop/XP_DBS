package core.parsing.util;

import exceptions.syntaxErrors.EndOfFileError;
import exceptions.syntaxErrors.SyntaxError;
import exceptions.syntaxErrors.TokenError;

import java.util.Queue;
import java.util.Set;

public class ComparatorConsumer {
    private static final Set<String> comparators = Set.of(
            "=",
            "!=",
            ">=",
            "<=",
            ">",
            "<"
    );

    private static boolean isComparator(String token) {
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
