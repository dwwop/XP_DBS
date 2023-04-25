package core.db.types;

public final class StringLiteral extends Literal<String> {

    public StringLiteral(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return "\"" + super.toString() + "\"";
    }
}
