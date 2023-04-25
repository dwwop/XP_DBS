package core.db.types;

public abstract sealed class Literal<T extends Comparable<T>>
        implements Comparable<Literal<T>>
        permits StringLiteral, IntegerLiteral {

    protected final T value;

    public enum Type {String, Integer}

    public Literal(T value) {
        this.value = value;
    }

    public boolean isNull() {
        return value == null;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public int compareTo(Literal<T> other) {
        if (isNull()) {
            return other.isNull() ? 0 : -1;
        }

        if (other.isNull()) {
            return -other.compareTo(this);
        }

        return value.compareTo(other.value);
    }

    public T getValue() {
        return value;
    }
}
