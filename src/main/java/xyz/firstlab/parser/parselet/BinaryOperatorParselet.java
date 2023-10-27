package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.Precedence;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.InfixExpression;
import xyz.firstlab.lexer.Token;

public class BinaryOperatorParselet implements InfixParselet {

    private final Precedence precedence;

    private final boolean isRight;

    public BinaryOperatorParselet(Precedence precedence, boolean isRight) {
        this.precedence = precedence;
        this.isRight = isRight;
    }

    @Override
    public Expression parse(Parser parser, Expression left) {
        Token token = parser.getCurToken();
        parser.nextToken();
        int precedenceValue = precedence.getValue() - (isRight ? 1 : 0);
        Expression right = parser.parseExpression(precedenceValue);
        return new InfixExpression(token, token.getLiteral(), left, right);
    }

    @Override
    public Precedence getPrecedence() {
        return precedence;
    }

}
