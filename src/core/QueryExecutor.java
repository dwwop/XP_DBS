package core;

import core.db.TableManager;
import core.parsing.Parser;
import core.parsing.tree.statements.Statement;

public class QueryExecutor {

    private final Parser parser = new Parser();
    private final TableManager tableManager = new TableManager();

    public Result execute(String query) {
        Statement statement = parser.parse(query);

        return statement.execute(tableManager);
    }
}
