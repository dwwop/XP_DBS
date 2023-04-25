package core.parsing.util;

import core.db.table.ColumnDefinition;
import exceptions.syntax.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.*;

public class ColumnConstraintExtractorTest {

    @Test
    public void noTokens() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>();

        Set<ColumnDefinition.Constraint> constraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        assertTrue(constraints.isEmpty());
    }

    @Test
    public void primaryKey() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("PRIMARY", "KEY"));

        Set<ColumnDefinition.Constraint> constraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        assertTrue(constraints.contains(ColumnDefinition.Constraint.PrimaryKey));
        assertEquals(1, constraints.size());
    }

    @Test
    public void notNull() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("NOT", "NULL"));

        Set<ColumnDefinition.Constraint> constraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        assertTrue(constraints.contains(ColumnDefinition.Constraint.NotNull));
        assertEquals(1, constraints.size());
    }

    @Test
    public void mixedCase() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("priMAry", "Key"));

        Set<ColumnDefinition.Constraint> constraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        assertTrue(constraints.contains(ColumnDefinition.Constraint.PrimaryKey));
        assertEquals(1, constraints.size());
    }

    @Test
    public void invalidConstraint() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("invalid"));

        Set<ColumnDefinition.Constraint> constraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        assertTrue(constraints.isEmpty());
        assertEquals("invalid", tokens.peek());
    }

    @Test
    public void noSpaceBetweenWords() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("notnull"));

        Set<ColumnDefinition.Constraint> constraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        assertTrue(constraints.isEmpty());
        assertEquals("notnull", tokens.peek());
    }

    @Test
    public void onlyFirstWord() {
        Queue<String> tokens = new LinkedList<>(List.of("not"));

        assertThrows(
                SyntaxError.class,
                () -> ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens)
        );
    }

    @Test
    public void invalidSecondWord() {
        Queue<String> tokens = new LinkedList<>(List.of("PRIMARY", "null"));

        assertThrows(
                SyntaxError.class,
                () -> ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens)
        );
    }

    @Test
    public void twoConstraints() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("PRIMARY", "KEY", "NOT", "NULL"));

        Set<ColumnDefinition.Constraint> constraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        assertTrue(constraints.contains(ColumnDefinition.Constraint.PrimaryKey));
        assertTrue(constraints.contains(ColumnDefinition.Constraint.NotNull));
        assertEquals(2, constraints.size());
    }

    @Test
    public void repetingConstraint() {
        Queue<String> tokens = new LinkedList<>(List.of("PRIMARY", "KEY", "PRIMARY", "KEY"));

        assertThrows(
                SyntaxError.class,
                () -> ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens)
        );
    }

    @Test
    public void emptyTokens() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("NOT", "", "", "NULL"));

        Set<ColumnDefinition.Constraint> constraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        assertTrue(constraints.contains(ColumnDefinition.Constraint.NotNull));
        assertEquals(1, constraints.size());
    }

    @Test
    public void startsWithEmptyTokens() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("", "", "", "NOT", "NULL"));

        Set<ColumnDefinition.Constraint> constraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        assertTrue(constraints.contains(ColumnDefinition.Constraint.NotNull));
        assertEquals(1, constraints.size());
    }

    @Test
    public void endsWithEmptyTokens() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("primary", "key", ""));

        Set<ColumnDefinition.Constraint> constraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        assertTrue(constraints.contains(ColumnDefinition.Constraint.PrimaryKey));
        assertEquals(1, constraints.size());
    }

    @Test
    public void endsWithInvalidConstraint() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("PRIMARY", "KEY", "invalid"));

        Set<ColumnDefinition.Constraint> constraints = ColumnConstraintExtractor.pollAllColumnConstraintsOrFail(tokens);

        assertTrue(constraints.contains(ColumnDefinition.Constraint.PrimaryKey));
        assertEquals(1, constraints.size());
        assertEquals("invalid", tokens.peek());
    }
}