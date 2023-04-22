package core.parsing.util;

import exceptions.SyntaxError;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class KeywordConsumer {

    public enum Keyword {
        TABLE,
        WHERE, ORDER, BY, LIMIT, OFFSET,
        INTO, VALUES,
        SET,
        FROM
    }

    public static boolean isKeyword(Keyword keyword, String token) {
        return token.toLowerCase().equals(keyword.toString().toLowerCase());
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
            throw new SyntaxError("The end of the query was reached but '" + keyword + "' was expected.");
        }

        String token = tokens.poll();

        if (!isKeyword(keyword, token)) {
            throw new SyntaxError("Found '" + token + "' but '" + keyword + "' was expected.");
        }
    }

    public static Keyword consumeKeywordOrFail(Set<Keyword> keywords, Queue<String> tokens) throws SyntaxError {
        String keywordsDisplayString = keywords.stream().map(Objects::toString).collect(Collectors.joining(", "));

        RawQueryTokenizer.consumeEmptyTokens(tokens);

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
}
