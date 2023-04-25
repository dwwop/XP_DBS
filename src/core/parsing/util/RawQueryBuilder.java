package core.parsing.util;

import java.util.Queue;

public class RawQueryBuilder {

    private final StringBuilder rawQuery = new StringBuilder();

    public RawQueryBuilder append(String token) {
        if (!rawQuery.isEmpty()) {
            rawQuery.append(" ");
        }

        rawQuery.append(token);

        return this;
    }

    public String build() {
        return rawQuery.toString();
    }

    public String buildRawTuple(Queue<String> tokens) {
        while (!tokens.isEmpty()) {
            String part = tokens.poll();

            append(part);

            if (part.endsWith(")")) {
                break;
            }
        }

        return build();
    }
}
