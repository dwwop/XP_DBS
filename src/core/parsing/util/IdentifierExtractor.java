package core.parsing.util;

import exceptions.SyntaxError;

import java.util.Map;
import java.util.Queue;

public class IdentifierExtractor {

    public enum Identifier {
        TableName, ColumnName
    }

    private static final Map<Identifier, String> identifierDisplayNames = Map.of(
        Identifier.TableName, "table name",
        Identifier.ColumnName, "column name"
    );

    private static String validateIdentifierOrFail(String token) throws SyntaxError {
        if (!token.matches("^[a-zA-Z]+[a-zA-Z1-9_-]*[a-zA-Z1-9]+$")) {
            throw new SyntaxError("Invalid identifier: '" + token + "'.");
        }

        return token;
    }

    public static String pollIdentifierOrFail(Identifier identifier, Queue<String> tokens) throws SyntaxError {
        RawQueryTokenizer.consumeEmptyTokens(tokens);

        if (tokens.isEmpty()) {
            throw new SyntaxError(
                "The end of the query was reached but a "
                + identifierDisplayNames.get(identifier)
                + " was expected."
            );
        }

        return validateIdentifierOrFail(tokens.poll());
    }
}
