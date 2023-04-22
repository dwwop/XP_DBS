package core.parsing.tree.statements.factories;

import core.db.table.ColumnDefinition;
import core.db.table.Schema;
import core.db.types.Literal;
import core.parsing.tree.statements.CreateTableStatement;
import core.parsing.tree.statements.Statement;
import core.parsing.util.*;
import exceptions.DatabaseError;
import exceptions.syntaxErrors.SyntaxError;
import exceptions.syntaxErrors.TokenError;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class CreateTableFactory extends StatementFactory {

    private Schema schema;

    @Override
    public Statement fromTokens(Queue<String> tokens) throws SyntaxError {
        schema = new Schema();

        String tableName = IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens);

        String rawSchema = new RawQueryBuilder().buildRawTuple(tokens);

        RawQueryTokenizer.consumeEmptyTokens(tokens);

        if (!tokens.isEmpty()) {
            throw new TokenError(tokens.peek(), "the end of the query");
        }

        Queue<String> rawColumnDefinitions = RawQueryTokenizer.tokenizeTupleTrimmingOrFail(
                RawQueryTokenizer.TupleType.Schema, rawSchema
        );

        while (!rawColumnDefinitions.isEmpty()) {
            parseRawColumnDefinition(rawColumnDefinitions.poll());
        }

        return new CreateTableStatement(tableName, schema);
    }

    private void parseRawColumnDefinition(String rawDefinition) throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(Arrays.asList(rawDefinition.split(" ")));

        String columnName = IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.ColumnName, tokens);
        Literal.Type dataType = LiteralExtractor.pollLiteralTypeOrFail(tokens);
        Set<ColumnDefinition.Constraint> columnConstraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        if (!tokens.isEmpty()) {
            throw new TokenError(tokens.peek(), "the end of the column definition");
        }

        if (schema.hasColumn(columnName)) {
            throw new SyntaxError("Multiple definitions of the column '" + columnName + "' found.");
        }

        try {
            schema.setColumnDefinition(columnName, new ColumnDefinition(dataType, columnConstraints));
        } catch (DatabaseError error) {
            throw new SyntaxError(error.getMessage());
        }
    }
}
