package core.db.table;

import core.db.types.Literal;

import java.util.Set;

public class ColumnDefinition {

    public enum Constraint { NotNull }

    private Literal.Type dataType;
    private Set<Constraint> constraints;

    public ColumnDefinition(Literal.Type dataType, Set<Constraint> constraints) {
        this.dataType = dataType;
        this.constraints = constraints;
    }

    public Literal.Type getDataType() {
        return dataType;
    }

    public Set<Constraint> getConstraints() {
        return constraints;
    }
}
