package core.parsing.tree.statements.factories;

import core.parsing.tree.statements.Statement;
import core.parsing.util.KeywordConsumer;
import exceptions.syntax.SyntaxError;

import java.util.Map;
import java.util.Queue;

public class CreateFactory extends StatementFactory {

    private static final Map<KeywordConsumer.Keyword, StatementFactory> createOptions = Map.of(
            KeywordConsumer.Keyword.TABLE, new CreateTableFactory()
    );

    @Override
    public Statement fromTokens(Queue<String> tokens) throws SyntaxError {
        KeywordConsumer.Keyword option = KeywordConsumer.consumeKeywordOrFail(createOptions.keySet(), tokens);

        return createOptions.get(option).fromTokens(tokens);
    }
}
