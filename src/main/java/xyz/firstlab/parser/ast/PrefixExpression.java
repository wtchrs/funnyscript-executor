package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

public class PrefixExpression implements Expression {

    private final Token token;

    private final String operator;

    private final Expression right;

    public PrefixExpression(Token token, String operator, Expression right) {
        this.token = token;
        this.operator = operator;
        this.right = right;
    }

    public String getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public String tokenLiteral() {
        return token.getLiteral();
    }

    @Override
    public String string() {
        if (operator.length() > 1) {
            return operator + " " + right.string();
        }
        return operator + right.string();
    }
}
