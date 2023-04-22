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

    public static boolean isKeyword(Keyword keyword, String token) {
        return token.equalsIgnoreCase(keyword.toString());
    }

    public static boolean consumeKeyword(Keyword keyword, Queue<String> tokens) {
        RawQueryTokenizer.consumeEmptyTokens(tokens);

        if (tokens.isEmpty() || !isKeyword(keyword, tokens.peek())) {
            return false;
        }

        tokens.poll();

        return true;
    }

    public static void consumeKeywordOrFail(Keyword keyword, Queue<String> tokens) throws SyntaxError {
        RawQueryTokenizer.consumeEmptyTokens(tokens);

        if (tokens.isEmpty()) {
            throw new EndOfFileError(keyword.toString());
        }

        String token = tokens.peek();

        if (!isKeyword(keyword, token)) {
            throw new TokenError(token, keyword.toString());
        }

        tokens.poll();
    }

    public static Keyword consumeKeywordOrFail(Set<Keyword> keywords, Queue<String> tokens) throws SyntaxError {
        String keywordsDisplayString = keywords.stream().map(Objects::toString).collect(Collectors.joining(", "));

        RawQueryTokenizer.consumeEmptyTokens(tokens);

        if (tokens.isEmpty()) {
            throw new EndOfFileError("one of " + keywordsDisplayString);
        }

        String token = tokens.peek();

        for (Keyword keyword : keywords) {
            if (isKeyword(keyword, token)) {
                tokens.poll();

                return keyword;
            }
        }

        throw new TokenError(token, "one of " + keywordsDisplayString);
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
        TABLE,
        INTO, VALUES,
        SET,
        FROM
    }
}
