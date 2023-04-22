package core.parsing.util;

import core.db.table.ColumnDefinition;
import core.db.types.IntegerLiteral;
import core.db.types.StringLiteral;
import exceptions.SyntaxError;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class ColumnConstraintExtractor {

    private static final Set<String> constraintFirstWords = Set.of(
        "primary", "not"
    );

    private static final Map<String, Set<String>> constraintSecondWords = Map.of(
        "primary", Set.of("key"),
        "not", Set.of("null")
    );

    private static final Map<String, ColumnDefinition.Constraint> constraints = Map.of(
        "primary key", ColumnDefinition.Constraint.PrimaryKey,
        "not null", ColumnDefinition.Constraint.NotNull
    );

    private static Set<ColumnDefinition.Constraint> currentConstraints;

    public static Set<ColumnDefinition.Constraint> pollAllColumnConstraintsOrFail(Queue<String> tokens) throws SyntaxError {
        currentConstraints = new HashSet<>();

        while (!tokens.isEmpty() && constraintFirstWords.contains(tokens.peek().toLowerCase())) {
            String firstWord = tokens.poll().toLowerCase();

            if (!isTwoWordConstraint(firstWord)) {
                addConstraint(firstWord);
                continue;
            }

            String secondWord = pollConstraintSecondWordOrFail(firstWord, tokens);

            addConstraint(firstWord + " " + secondWord);
        }

        return currentConstraints;
    }

    private static String pollConstraintSecondWordOrFail(String firstWord, Queue<String> tokens) throws SyntaxError {
        Set<String> secondWords = constraintSecondWords.get(firstWord);
        String secondWordsDisplayString = secondWords.stream().map(String::toUpperCase).collect(Collectors.joining(", "));

        if (tokens.isEmpty()) {
            throw new SyntaxError(
                "The end of the column definition was reached but one of '" + secondWordsDisplayString + "' was expected."
            );
        }

        String secondWord = tokens.peek().toLowerCase();

        if (!secondWords.contains(secondWord)) {
            throw new SyntaxError("Found '" + tokens.peek() + "' but '" + secondWordsDisplayString + "' was expected.");
        }

        return secondWord;
    }

    private static boolean isTwoWordConstraint(String firstWord) {
        return constraintSecondWords.containsKey(firstWord);
    }

    private static void addConstraint(String rawConstraint) throws SyntaxError {
        ColumnDefinition.Constraint constraint = constraints.get(rawConstraint);

        if (currentConstraints.contains(constraint)) {
            throw new SyntaxError("Multiple constraints of type '" + rawConstraint.toUpperCase() + "' fonund.");
        }

        currentConstraints.add(constraint);
    }
}
