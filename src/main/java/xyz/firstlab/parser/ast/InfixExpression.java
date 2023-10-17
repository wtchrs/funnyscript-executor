package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

public class InfixExpression extends Expression {

    private final String operator;

    private final Expression left;

    private final Expression right;

    public InfixExpression(Token token, String operator, Expression left, Expression right) {
        super(token);
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String getOperator() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public String string() {
        return String.format("%s %s %s", left.string(), operator, right.string());
    }
}
