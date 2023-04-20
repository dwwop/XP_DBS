package core.parsing;

import exceptions.SyntaxError;

import java.util.Queue;

public class KeywordConsumer {

    public enum Keyword {
        WHERE, ORDER, BY, LIMIT, OFFSET,
        INTO,
        SET,
        FROM
    }

    private static boolean isKeyword(Keyword keyword, String token) {
        return token.toLowerCase().equals(keyword.toString().toLowerCase());
    }

    public static boolean consumeKeyword(Keyword keyword, Queue<String> tokens) {
        if (tokens.isEmpty() || !isKeyword(keyword, tokens.peek())) {
            return false;
        }

        tokens.poll();

        return true;
    }

    public static void consumeKeywordOrFail(Keyword keyword, Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty()) {
            throw new SyntaxError("The end of the query was reached but '" + keyword + "' was expected.");
        }

        String token = tokens.poll();

        if (!isKeyword(keyword, token)) {
            throw new SyntaxError("Found '" + token + "' but '" + keyword + "' was expected.");
        }
    }
}
