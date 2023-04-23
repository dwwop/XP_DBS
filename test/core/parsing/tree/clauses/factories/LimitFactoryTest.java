package core.parsing.tree.clauses.factories;

import core.parsing.tree.clauses.LimitClause;
import exceptions.syntax.EndOfFileError;
import exceptions.syntax.SyntaxError;
import exceptions.syntax.TokenError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class LimitFactoryTest {
    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
                EndOfFileError.class,
                () -> new LimitFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "10"
        ));

        LimitClause clause = new LimitFactory().fromTokens(tokens);
        assertEquals(new LimitClause(10, 0), clause);
    }

    @Test
    public void fromTokensOffsetOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of(
                "10", "OFFSET", "5"
        ));

        LimitClause clause = new LimitFactory().fromTokens(tokens);
        assertEquals(new LimitClause(10, 5), clause);
    }

    @Test
    public void fromTokensMissingNumberRows() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "OFFSET", "5"
        ));
        assertThrows(TokenError.class, () -> new LimitFactory().fromTokens(tokens));
    }


    @Test
    public void fromTokensMissingOffsetVal() {
        Queue<String> tokens = new LinkedList<>(List.of(
                "10", "OFFSET"
        ));
        assertThrows(EndOfFileError.class, () -> new LimitFactory().fromTokens(tokens));
    }
}