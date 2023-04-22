package core.parsing.util;

import core.db.types.IntegerLiteral;
import core.db.types.Literal;
import core.db.types.StringLiteral;
import exceptions.SyntaxError;

import java.util.Map;
import java.util.Queue;

public class LiteralExtractor {

    private static final Map<String, Literal.Type> literalTypes = Map.of(
        "string", Literal.Type.String, "str", Literal.Type.String,
        "integer", Literal.Type.Integer, "int", Literal.Type.Integer
    );

    public static Literal pollLiteralOrFail(Queue<String> tokens) throws SyntaxError {
        RawQueryTokenizer.consumeEmptyTokens(tokens);

        if (tokens.isEmpty()) {
            throw new SyntaxError("The end of the query was reached but a literal was expected.");
        }

        String token = tokens.peek();

        if (token.matches("^\".*\"$")) {
            tokens.poll();

            return new StringLiteral(token.substring(1, token.length() - 1));
        }

        try {
            IntegerLiteral literal = new IntegerLiteral(Integer.valueOf(token));
            tokens.poll();

            return literal;
        } catch (NumberFormatException error) {
            throw new SyntaxError("Found '" + token + "' but a literal was expected.");
        }
    }

    public static Literal.Type pollLiteralTypeOrFail(Queue<String> tokens) throws SyntaxError {
        RawQueryTokenizer.consumeEmptyTokens(tokens);

        if (tokens.isEmpty()) {
            throw new SyntaxError("The end of the query was reached but a data type was expected.");
        }

        String token = tokens.peek().toLowerCase();

        if (!literalTypes.containsKey(token)) {
            throw new SyntaxError("Found '" + token + "' but a data type was expected.");
        }

        tokens.poll();

        return literalTypes.get(token);
    }
}
