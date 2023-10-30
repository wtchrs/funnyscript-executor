package xyz.firstlab.ast;

import xyz.firstlab.lexer.Token;

public abstract class Expression implements Node {

    private final Token token;

    public Expression(Token token) {
        this.token = token;
    }

    @Override
    public Token token() {
        return token;
    }

}
