package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

public class PrefixExpression extends Expression {

    private final String operator;

    private final Expression right;

    public PrefixExpression(Token token, String operator, Expression right) {
        super(token);
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
    public String string() {
        if (operator.length() > 1) {
            return String.format("(%s %s)", operator, right.string());
        }
        return String.format("(%s%s)", operator, right.string());
    }

}
