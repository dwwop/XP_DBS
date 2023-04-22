package util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StringsTest {

    @Test
    public void splitAndTrimOnTopLevelEmpty() {
        List<String> tokens = Strings.splitAndTrimOnTopLevel("", '|');

        assertTrue(tokens.isEmpty());
    }

    @Test
    public void splitAndTrimOnTopLevelSinglePartNoInnerDelimiters() {
        List<String> tokens = Strings.splitAndTrimOnTopLevel("only one", '|');

        assertEquals(1, tokens.size());
        assertEquals("only one", tokens.get(0));
    }

    @Test
    public void splitAndTrimOnTopLevelSinglePartWithInnerDelimiters() {
        List<String> tokens = Strings.splitAndTrimOnTopLevel("(only | one)", '|');

        assertEquals(1, tokens.size());
        assertEquals("(only | one)", tokens.get(0));
    }

    @Test
    public void splitAndTrimOnTopLevelMultiplePartsNoInnerDelimiters() {
        List<String> tokens = Strings.splitAndTrimOnTopLevel("one, another one, \"and another\"", ',');

        assertEquals(3, tokens.size());
        assertEquals("one", tokens.get(0));
        assertEquals("another one", tokens.get(1));
        assertEquals("\"and another\"", tokens.get(2));
    }

    @Test
    public void splitAndTrimOnTopLevelMultiplePartsNoInnerDelimitersEmptyPart() {
        List<String> tokens = Strings.splitAndTrimOnTopLevel("one, another one,, \"and another\"", ',');

        assertEquals(4, tokens.size());
        assertEquals("one", tokens.get(0));
        assertEquals("another one", tokens.get(1));
        assertEquals("", tokens.get(2));
        assertEquals("\"and another\"", tokens.get(3));
    }

    @Test
    public void splitAndTrimOnTopLevelMultiplePartsNoInnerDelimitersTrailingDelimiter() {
        List<String> tokens = Strings.splitAndTrimOnTopLevel("one, another one, \"and another\",", ',');

        assertEquals(4, tokens.size());
        assertEquals("one", tokens.get(0));
        assertEquals("another one", tokens.get(1));
        assertEquals("\"and another\"", tokens.get(2));
        assertEquals("", tokens.get(3));
    }

    @Test
    public void splitAndTrimOnTopLevelMultiplePartsWithInnerDelimiters() {
        List<String> tokens = Strings.splitAndTrimOnTopLevel("one, another one, \"and, another\"", ',');

        assertEquals(3, tokens.size());
        assertEquals("one", tokens.get(0));
        assertEquals("another one", tokens.get(1));
        assertEquals("\"and, another\"", tokens.get(2));
    }

    @Test
    public void splitAndTrimOnTopLevelDelimiterIsLevelDelimiter() {
        List<String> tokens = Strings.splitAndTrimOnTopLevel("one, another one, \"and, another\"", '"');

        assertEquals(3, tokens.size());
        assertEquals("one, another one,", tokens.get(0));
        assertEquals("and, another", tokens.get(1));
    }
}