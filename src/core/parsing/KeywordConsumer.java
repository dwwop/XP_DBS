package core.parsing;

import exceptions.syntaxErrors.EndOfFileError;
import exceptions.syntaxErrors.SyntaxError;
import exceptions.syntaxErrors.TokenError;

import java.util.Arrays;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static boolean isStatementKeyword(Queue<String> tokens) {
        Set<Keyword> statementKeywords = Arrays.stream(Keyword.values()).collect(Collectors.toSet());
        statementKeywords.removeAll(Set.of(Keyword.AND, Keyword.OR, Keyword.NOT));
        return statementKeywords.stream().map(Enum::toString).collect(Collectors.toSet()).contains(tokens.peek());
    }

    public enum Keyword {
        WHERE,
        AND, OR, NOT,
        ORDER, BY, ASC, DESC,
        LIMIT, OFFSET,
        INTO,
        SET,
        FROM
    }
}
