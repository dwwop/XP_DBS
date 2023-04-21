package core.parsing.tree.clauses;

import java.util.List;

public class ValuesClause extends Clause {

    private final List<List<String>> values;

    public ValuesClause(List<List<String>> values) {
        this.values = values;
    }

    public List<List<String>> getValues() {
        return values;
    }
}
