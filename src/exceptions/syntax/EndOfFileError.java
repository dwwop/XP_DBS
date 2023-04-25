package exceptions.syntax;

public class EndOfFileError extends SyntaxError {
    public EndOfFileError(String expectedToken) {
        super("The end of the query was reached but '" + expectedToken + "' was expected.");
    }
}
