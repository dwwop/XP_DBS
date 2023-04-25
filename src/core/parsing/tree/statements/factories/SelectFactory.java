package core.parsing.tree.statements.factories;

import core.parsing.tree.clauses.LimitClause;
import core.parsing.tree.clauses.OrderByClause;
import core.parsing.tree.clauses.SelectClause;
import core.parsing.tree.clauses.WhereClause;
import core.parsing.tree.clauses.factories.LimitFactory;
import core.parsing.tree.clauses.factories.OrderByFactory;
import core.parsing.tree.clauses.factories.SelectClauseFactory;
import core.parsing.tree.clauses.factories.WhereFactory;
import core.parsing.tree.statements.SelectStatement;
import core.parsing.util.IdentifierExtractor;
import core.parsing.util.KeywordConsumer;
import exceptions.syntax.SyntaxError;

import java.util.Queue;

public class SelectFactory extends StatementFactory {


    private static final WhereFactory whereFactory = new WhereFactory();
    private static final SelectClauseFactory selectFactory = new SelectClauseFactory();

    private static final LimitFactory limitFactory = new LimitFactory();

    private static final OrderByFactory orderByFactory = new OrderByFactory();

    @Override
    public SelectStatement fromTokens(Queue<String> tokens) throws SyntaxError {
        SelectClause selectClause = selectFactory.fromTokens(tokens);

        KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.FROM, tokens);

        String tableName = IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens);

        WhereClause whereClause = null;
        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.WHERE, tokens)) {
            whereClause = whereFactory.fromTokens(tokens);
        }

        OrderByClause orderByClause = null;
        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.ORDER, tokens)) {
            KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.BY, tokens);
            orderByClause = orderByFactory.fromTokens(tokens);
        }

        LimitClause limitClause = null;
        if (KeywordConsumer.consumeKeyword(KeywordConsumer.Keyword.LIMIT, tokens)) {
            limitClause = limitFactory.fromTokens(tokens);
        }

        if (!tokens.isEmpty()) {
            throw new SyntaxError("End of file expected but '" + tokens.poll() + "' found.");
        }

        return new SelectStatement(tableName, selectClause, whereClause, orderByClause, limitClause);
    }
}
