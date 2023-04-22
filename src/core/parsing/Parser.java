package core.parsing;

import core.parsing.tree.statements.Statement;
import core.parsing.tree.statements.factories.*;
import core.parsing.util.RawQueryTokenizer;
import exceptions.SyntaxError;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Parser {

    private static final Map<String, StatementFactory> statements = Map.of(
            "select", new SelectFactory(),
            "insert", new InsertFactory(),
            "update", new UpdateFactory(),
            "delete", new DeleteFactory(),
            "create", new CreateFactory()
    );

    public Statement parse(String query) throws SyntaxError {
//        query = query.replaceAll("\\(", " ( ");
//        query = query.replaceAll("\\)", " ) ");
//        Queue<String> tokens = new LinkedList<>(Arrays.asList(query.trim().split("\\s+")));

        Queue<String> tokens = RawQueryTokenizer.tokenizeQuery(query);
        if (tokens.isEmpty()) {
            throw new SyntaxError("Empty query.");
        }

        return parseStatement(tokens);
    }

    private Statement parseStatement(Queue<String> tokens) throws SyntaxError {
        String firstToken = tokens.poll().toLowerCase();

        if (!statements.containsKey(firstToken)) {
            throw new SyntaxError("Unknown statement: '" + firstToken.toUpperCase() + "'.");
        }

        return statements.get(firstToken).fromTokens(tokens);
    }
}
