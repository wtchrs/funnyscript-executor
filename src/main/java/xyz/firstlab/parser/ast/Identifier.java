package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

public class Identifier extends Expression {

    private final String value;

    public Identifier(Token token, String value) {
        super(token);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String string() {
        return value;
    }

}
