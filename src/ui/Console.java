package ui;

import core.QueryExecutor;
import core.commands.Result;
import core.db.table.ColumnDefinition;
import core.db.table.Row;
import core.db.table.Schema;
import core.db.table.Table;
import core.db.types.IntegerLiteral;
import core.db.types.Literal;
import core.db.types.StringLiteral;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Console {

    private final QueryExecutor queryExecutor = new QueryExecutor();

    public void run() throws IOException, InterruptedException {
        // TODO: implement the main loop

        printWelcomeMessage();

        while (true) {
            System.out.print("> ");

            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(System.in));
            String input = reader.readLine();

            switch (input) {
                case "/help" -> printHelpMessage();
                case "/exit" -> shutdownAppSequence(reader);

                default -> {
                    Result result = queryExecutor.execute(input);
                    if (result == null) printUnknownMessage();
                    if (!result.success()) {
                        System.err.println(result.message());
                    }
                    if (result.success()) {
                        returnTableString(result.output());
                    }
                }
            }


        }

    }

    public String returnTableString(Table table) throws InvalidObjectException {

        if (table == null) throw new InvalidObjectException("Table invalid");
        if (table.getSchema() == null || table.getSchema().getColumns().isEmpty()) throw new InvalidObjectException("Schema invalid");

        StringBuilder complete = new StringBuilder();
        Map<String, Integer> lengthOfColSpaces = getColumnSpaceSizes(table);
        int lengthOfLine = getColumnInfoRow(table, lengthOfColSpaces).length();

        complete.append(getLineDivider(lengthOfLine));
        complete.append(getColumnInfoRow(table, lengthOfColSpaces));
        complete.append(getLineDivider(lengthOfLine));

        for (Map.Entry<Literal, Row> row : table.getRows().entrySet()) {
            complete.append(getRowString(row.getValue(), table.getSchema(), lengthOfColSpaces));
            complete.append(getLineDivider(lengthOfLine));
        }

        return complete.toString();
    }

    private String getRowString(Row row, Schema schema, Map<String, Integer> lengthOfColSpaces) {
        StringBuilder complete = new StringBuilder("|");
        Map<String, List<String>> overflow = new HashMap<>();

        for (String colName : schema.getColumns().keySet()) {
            if (row.getValue(colName) == null) {
                complete.append(" ".repeat(lengthOfColSpaces.get(colName)));
            } else {
                String word = row.getValue(colName).getValue().toString();
                if (word.length() > lengthOfColSpaces.get(colName) - 2) {
                    List<String> splitted = new ArrayList<>(List.of(word.split("(?<=\\G.{" + (lengthOfColSpaces.get(colName) - 2) + "})")));
                    overflow.put(colName, splitted);
                    word = splitted.remove(0);
                }
                complete.append(" ").append(word);
                complete.append(" ".repeat(lengthOfColSpaces.get(colName) - word.length() - 1));
            }
            complete.append("|");
        }


        complete.append(getOverflownLines(overflow, schema, lengthOfColSpaces));
        return complete.toString();

    }

    private String getOverflownLines(Map<String, List<String>> overflow, Schema schema, Map<String, Integer> lengthOfColSpaces) {
        if (!overflow.isEmpty()) {
            StringBuilder complete = new StringBuilder();

            int numOfLines = overflow.values().stream().map(List::size).toList().stream().max(Integer::compare).get();
            for (int i = 0; i < numOfLines; i++) {
                complete.append("\n|");
                for (String colName : schema.getColumns().keySet()) {
                    if (overflow.get(colName) == null || i >= overflow.get(colName).size()) {
                        complete.append(" ".repeat(lengthOfColSpaces.get(colName)));
                    } else {
                        complete.append(" ").append(overflow.get(colName).get(i).toString());
                        complete.append(" ".repeat(lengthOfColSpaces.get(colName) - overflow.get(colName).get(i).toString().length() - 1));
                    }

                    complete.append("|");
                }
            }
            return complete.toString();
        }
        return "";
    }

    private String getColumnInfoRow(Table table, Map<String, Integer> colLengths) {
        Schema tableSchema = table.getSchema();
        StringBuilder complete = new StringBuilder();
        StringBuilder col = new StringBuilder();
        complete.append("|");
        for (Map.Entry<String,ColumnDefinition> entry : tableSchema.getColumns().entrySet()) {
            col.append(" ");
            col.append(entry.getKey());
            col.append(" ".repeat(colLengths.get(entry.getKey()) - col.length()));
            col.append("|");
            complete.append(col);
            col = new StringBuilder();
        }
        return complete.toString();
    }

    private Map<String, Integer> getColumnSpaceSizes(Table table) {
        Map <String, Integer> ret = new HashMap<>();

        for (String colName : table.getSchema().getColumns().keySet()) {
            ret.put(colName, colName.length());
        }

        for (Row row : table.getRows().values()) {
            for (String colName : table.getSchema().getColumns().keySet()) {
                if (row.getValue(colName)!=null && row.getValue(colName).getValue().toString().length() > ret.get(colName)) {
                    ret.replace(colName, row.getValue(colName).getValue().toString().length());
                }
            }
        }


        ret = ret.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        o -> Math.min(o.getValue() + 2, 35)));

        return ret;
    }

    private String getLineDivider(int length) {
        return "\n" + "-".repeat(length) + "\n";
    }

    public void shutdownAppSequence(BufferedReader reader) throws InterruptedException, IOException {
        System.out.print(">> Do you really want to exit? Y/N\n> ");
        String input = reader.readLine();

        if (Objects.equals(input, "N")) return;

        System.out.println("Logging out");
        Thread.sleep(100);
        System.out.println("Closing opened tables");
        Thread.sleep(150);
        System.out.println("Saving changes");
        Thread.sleep(130);
        System.out.println("Goodbye :)");
        Thread.sleep(200);

        System.exit(0);
    }

    public void printWelcomeMessage() {
        System.out.println("----------------------------------------------------------------------\n" +
                "> Extreme Database Systems are online | " + getCurrentTime() + "\n\n" +
                "> type your query after the \">\" symbol\n" +
                "> type /help for list of commands\n" +
                "> type /exit for list of commands\n" +
                "----------------------------------------------------------------------");
    }

    public void printHelpMessage() {
        System.out.println("list of commands\nTBA");
    }

    public void printUnknownMessage() {
        System.out.println(toItalic(toRed(">> Unknown command")));
    }

    public String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public String toItalic(String str) {
        String ITALIC_TEXT = "\033[3m";
        return ITALIC_TEXT + str + ITALIC_TEXT;
    }

    public String toRed(String str) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        return ANSI_RED + str + ANSI_RESET;
    }


}
