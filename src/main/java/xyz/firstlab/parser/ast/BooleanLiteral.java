package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

public class BooleanLiteral extends Expression {

    private final boolean value;

    public BooleanLiteral(Token token, boolean value) {
        super(token);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String string() {
        return Boolean.toString(value);
    }
}
