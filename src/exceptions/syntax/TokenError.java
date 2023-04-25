package exceptions.syntax;

public class TokenError extends SyntaxError {

    public TokenError(String token, String expectedToken) {
        super("Found '" + token + "' but '" + expectedToken + "' was expected.");
    }
}
