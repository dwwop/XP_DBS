package core.db.types;

public final class IntegerLiteral extends Literal<Integer> {

    public IntegerLiteral(Integer value) {
        super(value);
    }

    public Literal.Type getType(){
        return Type.Integer;
    }
}
