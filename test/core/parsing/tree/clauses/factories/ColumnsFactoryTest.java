package core.parsing.tree.clauses.factories;

import exceptions.syntax.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

public class ColumnsFactoryTest {

    @Test
    public void fromTokensEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
            SyntaxError.class,
            () -> new ColumnsFactory().fromTokens(tokens)
        );
    }

    @Test
    public void fromTokensEmptyTuple() {
        Queue<String> tokens = new LinkedList<>(List.of("(", ")", "continues"));

        assertThrows(
            SyntaxError.class,
            () -> new ColumnsFactory().fromTokens(tokens)
        );
        assertEquals(1, tokens.size());
    }

    @Test
    public void fromTokensOK() throws SyntaxError {
        Queue<String> tokens = new LinkedList<>(List.of("(one,", "t-w-o", ",", "t_h_r_e_e", ")", "continues"));
        List<String> columns = new ColumnsFactory().fromTokens(tokens).getColumns();

        assertEquals(3, columns.size());
        assertEquals("one", columns.get(0));
        assertEquals("t-w-o", columns.get(1));
        assertEquals("t_h_r_e_e", columns.get(2));
        assertEquals(1, tokens.size());
    }

    @Test
    public void fromTokensInvalidNames() {
        Queue<String> tokens = new LinkedList<>(List.of("(one,", "t-w-/o", ",", "t_h_r_e_e", ")", "continues"));

        assertThrows(
            SyntaxError.class,
            () -> new ColumnsFactory().fromTokens(tokens)
        );
        assertEquals(1, tokens.size());
    }
}