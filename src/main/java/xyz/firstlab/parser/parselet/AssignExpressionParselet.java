package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.AssignExpressionUtils;
import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.Precedence;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.InfixExpression;
import xyz.firstlab.token.Token;

public class AssignExpressionParselet implements InfixParselet {

    @Override
    public Expression parse(Parser parser, Expression left) {
        // left must be a function or identifier.
        AssignExpressionUtils.assertAssignable(left);

        Token curToken = parser.getCurToken();
        parser.nextToken();
        Expression right = parser.parseExpression(getPrecedence().getValue() - 1);

        return new InfixExpression(curToken, curToken.getLiteral(), left, right);
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.ASSIGN;
    }

}
