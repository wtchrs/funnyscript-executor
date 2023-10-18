package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.Precedence;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.PrefixExpression;
import xyz.firstlab.token.Token;

public class PrefixOperatorParselet implements PrefixParselet {

    @Override
    public Expression parse(Parser parser) {
        Token curToken = parser.getCurToken();
        parser.nextToken();
        Expression right = parser.parseExpression(Precedence.PREFIX);
        return new PrefixExpression(curToken, curToken.getLiteral(), right);
    }

}
