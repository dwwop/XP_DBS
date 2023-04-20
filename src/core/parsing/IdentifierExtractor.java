package core.parsing;

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

    public static String pollIdentifierOrFail(Identifier identifier, Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty()) {
            throw new SyntaxError(
                "The end of the query was reached but a "
                + identifierDisplayNames.get(identifier)
                + " was expected."
            );
        }

        return tokens.poll();
    }
}
