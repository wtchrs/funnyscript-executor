package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.Precedence;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.InfixExpression;
import xyz.firstlab.token.Token;

public class BinaryOperatorParselet implements InfixParselet {

    private final Precedence precedence;

    public BinaryOperatorParselet(Precedence precedence) {
        this.precedence = precedence;
    }

    @Override
    public Expression parse(Parser parser, Expression left) {
        Token token = parser.getCurToken();
        parser.nextToken();
        Expression right = parser.parseExpression(Precedence.LOWEST);
        return new InfixExpression(token, token.getLiteral(), left, right);
    }

    @Override
    public Precedence getPrecedence() {
        return precedence;
    }

}
