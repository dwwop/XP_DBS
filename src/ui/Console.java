package ui;

import core.QueryExecutor;
import core.commands.Result;
import core.db.table.Table;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Console {


    public final String ANSI_RESET = "\u001B[0m";
    public final String ANSI_RED = "\u001B[31m";
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
                    if (!result.success()) printUnknownMessage();
                }
            }


        }

    }

    public String returnTableString(Table table) {

        return "ahoj";
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
        return ANSI_RED + str + ANSI_RESET;
    }


}
