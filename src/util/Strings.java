package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Strings {

    public static List<String> splitOnTopLevel(String sequence, Set<Character> levelDelimiteres, char delimiter) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        boolean isTopLevel = true;

        for (char ch : sequence.toCharArray()) {
            if (ch == delimiter && isTopLevel) {
                parts.add(currentPart.toString());
                currentPart.setLength(0);
                continue;
            }

            if (levelDelimiteres.contains(ch)) {
                isTopLevel = !isTopLevel;
            }

            currentPart.append(ch);
        }

        return parts;
    }

    public static List<String> splitAndTrim(String sequence, String regex) {
        List<String> parts = new ArrayList<>();

        for (String part : sequence.split(regex)) {
            parts.add(part.trim());
        }

        return parts;
    }
}
