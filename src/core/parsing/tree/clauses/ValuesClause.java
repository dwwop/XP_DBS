package core.parsing.tree.clauses;

import core.db.types.Literal;

import java.util.List;

public class ValuesClause extends Clause {

    private final List<List<Literal>> values;

    public ValuesClause(List<List<Literal>> values) {
        this.values = values;
    }

    public List<List<Literal>> getValues() {
        return values;
    }
}
