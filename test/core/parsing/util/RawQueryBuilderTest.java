package core.parsing.util;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

public class RawQueryBuilderTest {

    @Test
    public void buildEmpty() {
        assertEquals("", new RawQueryBuilder().build());
    }

    @Test
    public void buildSinglePart() {
        String query = new RawQueryBuilder().append("one").build();

        assertEquals("one", query);
    }

    @Test
    public void buildMultipleParts() {
        String query = new RawQueryBuilder().append("one").append("\"two\"").build();

        assertEquals("one \"two\"", query);
    }

    @Test
    public void buildAppendingEmpty() {
        String query = new RawQueryBuilder().append("one").append("\"two").append("").append("\"").build();

        assertEquals("one \"two  \"", query);
    }

    @Test
    public void buildTupleEmpty() {
        Queue<String> tokens = new LinkedList<>();

        assertEquals("", new RawQueryBuilder().buildRawTuple(tokens));
    }

    @Test
    public void buildTupleSingleElement() {
        Queue<String> tokens = new LinkedList<>(List.of("(one)"));

        assertEquals("(one)", new RawQueryBuilder().buildRawTuple(tokens));
        assertEquals(0, tokens.size());
    }

    @Test
    public void buildTupleMultipleElements() {
        Queue<String> tokens = new LinkedList<>(List.of("(one,", "two,", "three)", "", "not a tuple"));

        assertEquals("(one, two, three)", new RawQueryBuilder().buildRawTuple(tokens));
        assertEquals(2, tokens.size());
    }

    @Test
    public void buildTupleMultipleElementsEmptyToken() {
        Queue<String> tokens = new LinkedList<>(List.of("(one,", "two,", "", "three)", "not a tuple"));

        assertEquals("(one, two,  three)", new RawQueryBuilder().buildRawTuple(tokens));
        assertEquals(1, tokens.size());
    }

    @Test
    public void buildTupleMultipleElementsNoParentheses() {
        Queue<String> tokens = new LinkedList<>(List.of("one,", "two,", "three", ""));

        assertEquals("one, two, three ", new RawQueryBuilder().buildRawTuple(tokens));
        assertEquals(0, tokens.size());
    }
}