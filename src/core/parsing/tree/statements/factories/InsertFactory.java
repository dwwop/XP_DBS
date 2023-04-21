package core.parsing.tree.statements.factories;

import core.parsing.IdentifierExtractor;
import core.parsing.KeywordConsumer;
import core.parsing.tree.clauses.ColumnsClause;
import core.parsing.tree.clauses.ValuesClause;
import core.parsing.tree.clauses.factories.ColumnsFactory;
import core.parsing.tree.clauses.factories.ValuesFactory;
import core.parsing.tree.statements.InsertStatement;
import exceptions.SyntaxError;

import java.util.Comparator;
import java.util.List;
import java.util.Queue;

public class InsertFactory extends StatementFactory {

    private static final ColumnsFactory columnsFactory = new ColumnsFactory();
    private static final ValuesFactory valuesFactory = new ValuesFactory();

    @Override
    public InsertStatement fromTokens(Queue<String> tokens) throws SyntaxError {
        KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.INTO, tokens);

        String tableName = IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens);

        ColumnsClause columnsClause = columnsFactory.fromTokens(tokens);

        KeywordConsumer.consumeKeywordOrFail(KeywordConsumer.Keyword.VALUES, tokens);

        ValuesClause valuesClause = valuesFactory.fromTokens(tokens);

        int columnsListSize = columnsClause.getColumns().size();
        int tupleSize = valuesClause.getValues().get(0).size();

        if (columnsListSize != tupleSize) {
            throw new SyntaxError(
                "The list of columns and the value tuples have a different size ("
                + columnsListSize + " vs " + tupleSize + ")."
            );
        }

        return new InsertStatement(tableName, columnsClause, valuesClause);
    }
}
