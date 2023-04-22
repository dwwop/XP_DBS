package core.parsing.util;

import exceptions.SyntaxError;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

public class IdentifierExtractorTest {

    @Test
    public void noTokens() {
        Queue<String> tokens = new LinkedList<>();

        assertThrows(
            SyntaxError.class,
            () -> IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens)
        );
    }

    @Test
    public void tableName() throws SyntaxError {
        String expected = "table-name";
        Queue<String> tokens = new LinkedList<>(List.of(expected));

        String identifier = IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens);

        assertEquals(expected, identifier);
    }

    @Test
    public void columnName() throws SyntaxError {
        String expected = "column_Name";
        Queue<String> tokens = new LinkedList<>(List.of(expected));

        String identifier = IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.ColumnName, tokens);

        assertEquals(expected, identifier);
    }

    @Test
    public void identifierWithInvalidCharacter() {
        Queue<String> tokens = new LinkedList<>(List.of("only-lower-UPPER-dash-underscore_no-special#"));

        assertThrows(
            SyntaxError.class,
            () -> IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens)
        );
    }

    @Test
    public void identifierWithInvalidStart() {
        Queue<String> tokens = new LinkedList<>(List.of("---starting-with-dash-forbidden"));

        assertThrows(
            SyntaxError.class,
            () -> IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens)
        );
    }

    @Test
    public void identifierWithInvalidEnd() {
        Queue<String> tokens = new LinkedList<>(List.of("ending-with-underscore-forbidden_"));

        assertThrows(
            SyntaxError.class,
            () -> IdentifierExtractor.pollIdentifierOrFail(IdentifierExtractor.Identifier.TableName, tokens)
        );
    }
}