package core.db.types;

public final class StringLiteral extends Literal<String> {

    public StringLiteral(String value) {
        super(value);
    }
    public Literal.Type getType(){
        return Type.String;
    }
    @Override
    public String toString() {
        return "\"" + super.toString() + "\"";
    }
}
