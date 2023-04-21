package core.parsing.tree.statements.factories;

import core.parsing.IdentifierExtractor;
import core.parsing.KeywordConsumer;
import core.parsing.tree.clauses.SetClause;
import core.parsing.tree.clauses.WhereClause;
import core.parsing.tree.clauses.factories.SetFactory;
import core.parsing.tree.clauses.factories.WhereFactory;
import core.parsing.tree.statements.UpdateStatement;
import exceptions.syntaxErrors.SyntaxError;

import java.util.Queue;

public class UpdateFactory extends StatementFactory {

    private static final WhereFactory whereFactory = new WhereFactory();
    private static final SetFactory setFactory = new SetFactory();

    @Override
    public UpdateStatement fromTokens(Queue<String> tokens) throws SyntaxError {
        String tableName = IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens);

        KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.SET, tokens);

        SetClause setClause = setFactory.fromTokens(tokens);
        WhereClause whereClause = whereFactory.getEmptyClause();

        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.WHERE, tokens)) {
            whereClause = whereFactory.fromTokens(tokens);
        }

        return new UpdateStatement(tableName, whereClause, setClause);
    }
}
