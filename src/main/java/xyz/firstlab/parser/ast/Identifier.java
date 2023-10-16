package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

public class Identifier implements Expression {

    private final Token token;

    private final String value;

    public Identifier(Token token, String value) {
        this.token = token;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String tokenLiteral() {
        return token.getLiteral();
    }

    @Override
    public String string() {
        return value;
    }

}
