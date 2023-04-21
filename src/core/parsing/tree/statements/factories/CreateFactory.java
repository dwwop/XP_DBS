package core.parsing.tree.statements.factories;

import core.db.table.Schema;
import core.parsing.IdentifierExtractor;
import core.parsing.KeywordConsumer;
import core.parsing.tree.statements.CreateTableStatement;
import core.parsing.tree.statements.Statement;
import exceptions.SyntaxError;

import java.util.List;
import java.util.Queue;

public class CreateFactory extends StatementFactory {

    private static final List<KeywordConsumer.Keyword> createOptions = List.of(
        KeywordConsumer.Keyword.TABLE
    );

    @Override
    public Statement fromTokens(Queue<String> tokens) throws SyntaxError {
        return switch (KeywordConsumer.consumeKeywordOrFail(createOptions, tokens)) {
            case TABLE -> parseCreateTable(tokens);
            default -> throw new SyntaxError("Unknown CREATE statement.");
        };
    }

    private CreateTableStatement parseCreateTable(Queue<String> tokens) throws SyntaxError {
        String tableName = IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens);

        return new CreateTableStatement(new Schema());
    }
}
