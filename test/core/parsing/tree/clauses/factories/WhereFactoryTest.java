package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.WhereClause;
import core.parsing.tree.clauses.conditions.AndCondition;
import core.parsing.tree.clauses.conditions.Expression;
import core.parsing.tree.clauses.conditions.NotCondition;
import core.parsing.tree.clauses.conditions.OrCondition;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class WhereFactoryTest {
    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
                EndOfFileError.class,
                () -> new WhereFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensSimpleExprOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col", "=", "val", "ORDER"
        ));

        WhereClause clause = new WhereFactory().fromTokens(tokens);
        assertEquals(new WhereClause(new Expression("col", "=", "val")), clause);
        assertEquals("ORDER", tokens.poll());
    }

    @Test
    public void fromTokensSimpleExprStringOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col", "=", "\"val\"", "ORDER"
        ));

        WhereClause clause = new WhereFactory().fromTokens(tokens);
        assertEquals(new WhereClause(new Expression("col", "=", "val")), clause);
        assertEquals("ORDER", tokens.poll());
    }

    @Test
    public void fromTokensSimpleExprStringUnclosed() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col", "=", "\"val", "ORDER"
        ));

        assertThrows(SyntaxError.class, () -> new WhereFactory().fromTokens(tokens));
    }

    @Test
    public void fromTokensMissingColumnName() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "=", "val"
        ));

        assertThrows(TokenError.class, () -> new WhereFactory().fromTokens(tokens));
    }


    @Test
    public void fromTokensMissingVal() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col", "="
        ));

        assertThrows(EndOfFileError.class, () -> new WhereFactory().fromTokens(tokens));
    }

    @Test
    public void fromTokensMultipleComparators() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col", "=", "<", "val"
        ));

        assertThrows(TokenError.class, () -> new WhereFactory().fromTokens(tokens));
    }

    @Test
    public void fromTokensSimpleNot() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "NOT", "col", "!=", "val", "ORDER"
        ));

        WhereClause clause = new WhereFactory().fromTokens(tokens);
        assertEquals(new WhereClause(new NotCondition(new Expression("col", "!=", "val"))), clause);
        assertEquals("ORDER", tokens.poll());
    }

    @Test
    public void fromTokensSimpleAnd() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col", ">", "val", "AND", "col", "<", "val", "ORDER"
        ));

        WhereClause clause = new WhereFactory().fromTokens(tokens);
        assertEquals(new WhereClause(new AndCondition(new Expression("col", ">", "val"), new Expression("col", "<", "val"))), clause);
        assertEquals("ORDER", tokens.poll());
    }


    @Test
    public void fromTokensSimpleOR() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "col", ">=", "val", "OR", "col", "<=", "val", "ORDER"
        ));

        WhereClause clause = new WhereFactory().fromTokens(tokens);
        assertEquals(new WhereClause(new OrCondition(new Expression("col", ">=", "val"), new Expression("col", "<=", "val"))), clause);
        assertEquals("ORDER", tokens.poll());
    }

    @Test
    public void fromTokensSimpleExprNested() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "(", "(", "col", "=", "val", ")", ")", "ORDER"
        ));

        WhereClause clause = new WhereFactory().fromTokens(tokens);
        assertEquals(new WhereClause(new Expression("col", "=", "val")), clause);
        assertEquals("ORDER", tokens.poll());
    }

    @Test
    public void fromTokensUnclosedBracket() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "(", "(", "col", "=", "val", ")", "ORDER"
        ));


        assertThrows(TokenError.class, () -> new WhereFactory().fromTokens(tokens));
    }

    @Test
    public void fromTokensUnopenedBracket() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "(", "col", "=", "val", ")", ")"
        ));

        WhereClause clause = new WhereFactory().fromTokens(tokens);
        assertEquals(new WhereClause(new Expression("col", "=", "val")), clause);
        assertEquals(")", tokens.poll());
    }

    @Test
    public void fromTokensNewNestedExprOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "NOT", "(", "col", "=", "val", "AND", "col", "=", "val", ")", "OR", "(", "col", "=", "val", "AND", "col", "=", "val", ")", "ORDER"
        ));

        WhereClause clause = new WhereFactory().fromTokens(tokens);
        assertEquals(new WhereClause(new NotCondition(
                new OrCondition(
                        new AndCondition(
                                new Expression("col", "=", "val"),
                                new Expression("col", "=", "val")
                        ),
                        new AndCondition(
                                new Expression("col", "=", "val"),
                                new Expression("col", "=", "val")
                        )
                )
        )), clause);
        assertEquals("ORDER", tokens.poll());
    }

    @Test
    public void fromTokensMissingExpr() {
        Queue<String> andTokens = new LinkedList<>(List.of(
                "col", "=", "val", "AND", "ORDER"));

        assertThrows(TokenError.class, () -> new WhereFactory().fromTokens(andTokens));

        Queue<String> orTokens = new LinkedList<>(List.of(
                "col", "=", "val", "OR", "ORDER"));

        assertThrows(TokenError.class, () -> new WhereFactory().fromTokens(orTokens));

        Queue<String> notTokens = new LinkedList<>(List.of(
                "col", "=", "val", "OR", "NOT", "ORDER"));

        assertThrows(TokenError.class, () -> new WhereFactory().fromTokens(notTokens));

        Queue<String> bracketTokens = new LinkedList<>(List.of(
                "col", "=", "val", "OR", "(", "ORDER"));

        assertThrows(TokenError.class, () -> new WhereFactory().fromTokens(bracketTokens));
    }

    @Test
    public void fromTokensEmptyExpr() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "(", "col", "=", "val", ")", "(", ")", "ORDER"));

        assertThrows(TokenError.class, () -> new WhereFactory().fromTokens(tokens));
    }
}