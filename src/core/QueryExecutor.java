package core;

import core.db.TableManager;
import core.parsing.Parser;
import core.parsing.tree.statements.Statement;
import exceptions.syntaxErrors.SyntaxError;

public class QueryExecutor {

    private final Parser parser = new Parser();
    private final TableManager tableManager = new TableManager();

    public Result execute(String query) {
        try {
            Statement statement = parser.parse(query);

            return statement.execute(tableManager);
        } catch (SyntaxError error) {
            return new Result(false, error.getMessage(), null);
        }
    }
}
