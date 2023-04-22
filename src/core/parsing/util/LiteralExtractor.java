package core.parsing.util;

import core.db.types.IntegerLiteral;
import core.db.types.Literal;
import core.db.types.StringLiteral;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;
import util.Strings;

import java.util.ArrayList;
import java.util.List;
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
            return new StringLiteral(String.join("", token.substring(1, token.length() - 1)));
        }
//        if (token.startsWith("\"")) {
//            List<String> stringLiteralVal = new ArrayList<>();
//            stringLiteralVal.add(token);
//            while (!token.endsWith("\"")) {
//                token = tokens.poll();
//                stringLiteralVal.add(token);
//                if (token == null)
//                    throw new EndOfFileError("literal");
//            }
//            stringLiteralVal.add(token);
//            stringLiteralVal.set(0, stringLiteralVal.get(0).substring(1));
//            String last = stringLiteralVal.get(0);
//            stringLiteralVal.set(stringLiteralVal.size() - 1, last.substring(0, last.length() - 1));
//            return new StringLiteral(String.join("", stringLiteralVal));
//        }

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
