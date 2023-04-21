package core.db.table;

import core.db.types.Literal;

import java.util.Set;

public class ColumnDefinition {

    private final Literal.Type dataType;
    private final Set<Constraint> constraints;

    public ColumnDefinition(Literal.Type dataType, Set<Constraint> constraints) {
        this.dataType = dataType;
        this.constraints = constraints;
    }

    public enum Constraint {NotNull}
}
