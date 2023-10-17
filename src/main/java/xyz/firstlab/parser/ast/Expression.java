package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

public abstract class Expression implements Node {

    private final Token token;

    public Expression(Token token) {
        this.token = token;
    }

    @Override
    public String tokenLiteral() {
        return token.getLiteral();
    }

}
