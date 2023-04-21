package core.parsing;

import core.db.types.IntegerLiteral;
import core.db.types.Literal;
import core.db.types.StringLiteral;
import exceptions.SyntaxError;

import java.util.Map;
import java.util.Queue;

public class LiteralExtractor {

    public static Literal pollLiteralOrFail(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty()) {
            throw new SyntaxError("The end of the query was reached but a literal was expected.");
        }

        String token = tokens.poll();

        if (token.matches("^\".*\"$")) {
            return new StringLiteral(token.substring(1, token.length() - 1));
        }

        try {
            return new IntegerLiteral(Integer.valueOf(token));
        } catch (NumberFormatException error) {
            throw new SyntaxError("Found '" + token + "' but a literal was expected.");
        }
    }
}
