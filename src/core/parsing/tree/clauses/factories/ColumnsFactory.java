package core.parsing.tree.clauses.factories;

import core.parsing.util.IdentifierExtractor;
import core.parsing.util.RawQueryBuilder;
import core.parsing.util.RawQueryTokenizer;
import core.parsing.tree.clauses.ColumnsClause;
import exceptions.SyntaxError;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ColumnsFactory extends ClauseFactory {

    @Override
    public ColumnsClause fromTokens(Queue<String> tokens) throws SyntaxError {
        if (tokens.isEmpty()) {
            throw new SyntaxError("The end of the query was reached but a list of columns was expected.");
        }

        String rawColumnsClause = new RawQueryBuilder().buildRawTuple(tokens);

        Queue<String> columnsList = RawQueryTokenizer.tokenizeTupleTrimmingOrFail(
            RawQueryTokenizer.TupleType.ColumnsList, rawColumnsClause
        );

        List<String> columnNames = parseColumnsList(columnsList);

        return new ColumnsClause(columnNames);
    }

    private List<String> parseColumnsList(Queue<String> columnsList) throws SyntaxError {
        List<String> columnNames = new ArrayList<>();

        while (!columnsList.isEmpty()) {
            columnNames.add(IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.ColumnName, columnsList));
        }

        return columnNames;
    }
}
