package core.parsing.tree.statements.factories;

import core.parsing.tree.clauses.SetClause;
import core.parsing.tree.clauses.WhereClause;
import core.parsing.tree.clauses.factories.SetFactory;
import core.parsing.tree.clauses.factories.WhereFactory;
import core.parsing.tree.statements.UpdateStatement;
import core.parsing.util.IdentifierExtractor;
import core.parsing.util.KeywordConsumer;
import core.parsing.util.RawQueryTokenizer;
import exceptions.syntax.SyntaxError;

import java.util.Queue;

public class UpdateFactory extends StatementFactory {

    private static final WhereFactory whereFactory = new WhereFactory();
    private static final SetFactory setFactory = new SetFactory();

    @Override
    public UpdateStatement fromTokens(Queue<String> tokens) throws SyntaxError {
        String tableName = IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens);

        KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.SET, tokens);

        SetClause setClause = setFactory.fromTokens(tokens);
        WhereClause whereClause = null;

        RawQueryTokenizer.consumeEmptyTokens(tokens);

        if (!tokens.isEmpty()) {
            KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.WHERE, tokens);

            whereClause = whereFactory.fromTokens(tokens);
        }

        return new UpdateStatement(tableName, whereClause, setClause);
    }
}
