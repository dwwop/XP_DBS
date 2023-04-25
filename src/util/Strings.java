package util;

import java.util.ArrayList;
import java.util.List;

public class Strings {

    private static final List<Character> startingLevelDelimiteres = List.of('"', '(');
    private static final List<Character> endingLevelDelimiteres = List.of('"', ')');

    public static List<String> splitAndTrimOnTopLevel(String sequence, char delimiter) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        int levelDelimiterIndex = -1;

        for (char ch : sequence.toCharArray()) {
            if (levelDelimiterIndex < 0) {
                if (ch == delimiter) {
                    parts.add(currentPart.toString().trim());
                    currentPart.setLength(0);
                    continue;
                }

                levelDelimiterIndex = startingLevelDelimiteres.indexOf(ch);
            } else if (endingLevelDelimiteres.get(levelDelimiterIndex).equals(ch)) {
                levelDelimiterIndex = -1;
            }

            currentPart.append(ch);
        }

        if ((!sequence.isEmpty() && sequence.charAt(sequence.length() - 1) == delimiter) || !currentPart.isEmpty()) {
            parts.add(currentPart.toString().trim());
        }

        return parts;
    }
}
