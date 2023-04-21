package core.parsing.tree.statements.factories;

import core.parsing.IdentifierExtractor;
import core.parsing.KeywordConsumer;
import core.parsing.tree.clauses.WhereClause;
import core.parsing.tree.clauses.factories.WhereFactory;
import core.parsing.tree.statements.DeleteStatement;
import exceptions.SyntaxError;

import java.util.Queue;

public class DeleteFactory extends StatementFactory {

    private static final WhereFactory whereFactory = new WhereFactory();

    @Override
    public DeleteStatement fromTokens(Queue<String> tokens) throws SyntaxError {
        KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.FROM, tokens);

        String tableName = IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens);

        WhereClause whereClause = whereFactory.getEmptyClause();

        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.WHERE, tokens)) {
            whereClause = whereFactory.fromTokens(tokens);
        }

        return new DeleteStatement(tableName, whereClause);
    }
}
