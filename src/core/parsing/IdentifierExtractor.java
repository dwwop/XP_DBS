package core.parsing;

import exceptions.syntaxErrors.EndOfFileError;
import exceptions.syntaxErrors.SyntaxError;

import java.util.Map;
import java.util.Queue;

public class IdentifierExtractor {

    private static final Map<Identifier, String> identifierDisplayNames = Map.of(
            Identifier.TableName, "table name",
            Identifier.ColumnName, "column name"
    );

    public static String pollIdentifierOrFail(Identifier identifier, Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty()) {
            throw new EndOfFileError(identifierDisplayNames.get(identifier));
        }

        return tokens.poll();
    }

    public enum Identifier {
        TableName, ColumnName
    }
}
