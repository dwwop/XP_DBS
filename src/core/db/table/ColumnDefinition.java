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

    public Literal.Type getDataType() {
        return dataType;
    }

    public boolean hasConstraint(Constraint constraint) {
        return constraints.contains(constraint);
    }

    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }

    public void removeConstraint(Constraint constraint) {
        constraints.remove(constraint);
    }

    public enum Constraint {PrimaryKey, NotNull}
}
