package core.parsing;

import exceptions.syntaxErrors.EndOfFileError;
import exceptions.syntaxErrors.SyntaxError;
import exceptions.syntaxErrors.TokenError;

import java.util.Queue;

public class KeywordConsumer {

    private static boolean isKeyword(Keyword keyword, String token) {
        return token.equalsIgnoreCase(keyword.toString());
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
            throw new EndOfFileError(keyword.toString());
        }

        String token = tokens.poll();

        if (!isKeyword(keyword, token)) {
            throw new TokenError(token, keyword.toString());
        }
    }

    public enum Keyword {
        WHERE,
        ORDER, BY, ASC, DESC,
        LIMIT, OFFSET,
        INTO,
        SET,
        FROM
    }
}
