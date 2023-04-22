package core.parsing.util;

import exceptions.syntaxErrors.EndOfFileError;
import exceptions.syntaxErrors.SyntaxError;
import exceptions.syntaxErrors.TokenError;

import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class KeywordConsumer {

    public enum Keyword {
        WHERE,
        AND, OR, NOT,
        ORDER, BY, ASC, DESC,
        LIMIT, OFFSET,
        TABLE,
        INTO, VALUES,
        SET,
        FROM
    }

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

    public static Keyword consumeKeywordOrFail(Set<Keyword> keywords, Queue<String> tokens) throws SyntaxError {
        String keywordsDisplayString = keywords.stream().map(Objects::toString).collect(Collectors.joining(", "));

        if (tokens.isEmpty()) {
            throw new SyntaxError("The end of the query was reached but one of '" + keywordsDisplayString + "' was expected.");
        }

        String token = tokens.poll();

        for (Keyword keyword : keywords) {
            if (isKeyword(keyword, token)) {
                return keyword;
            }
        }

        throw new SyntaxError("Found '" + token + "' but one of '" + keywordsDisplayString + "' was expected.");
    }

    public static boolean isStatementKeyword(Queue<String> tokens) {
        Set<Keyword> statementKeywords = Arrays.stream(Keyword.values()).collect(Collectors.toSet());
        statementKeywords.removeAll(Set.of(Keyword.AND, Keyword.OR, Keyword.NOT));
        return statementKeywords.stream().map(Enum::toString).collect(Collectors.toSet()).contains(tokens.peek());
    }
}
