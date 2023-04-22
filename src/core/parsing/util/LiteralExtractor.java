package core.parsing.util;

import core.db.types.IntegerLiteral;
import core.db.types.Literal;
import core.db.types.StringLiteral;
import exceptions.syntaxErrors.EndOfFileError;
import exceptions.syntaxErrors.SyntaxError;
import exceptions.syntaxErrors.TokenError;

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
            throw new EndOfFileError("literal");
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
            throw new TokenError(token, "literal");
        }
    }

    public static Literal.Type pollLiteralTypeOrFail(Queue<String> tokens) throws SyntaxError {
        RawQueryTokenizer.consumeEmptyTokens(tokens);

        if (tokens.isEmpty()) {
            throw new EndOfFileError("data type");
        }

        String token = tokens.peek().toLowerCase();

        if (!literalTypes.containsKey(token)) {
            throw new TokenError(token, "data type");
        }

        tokens.poll();

        return literalTypes.get(token);
    }
}
