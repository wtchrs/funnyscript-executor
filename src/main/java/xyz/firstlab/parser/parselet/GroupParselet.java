package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.Precedence;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.token.TokenType;

public class GroupParselet implements PrefixParselet {

    @Override
    public Expression parse(Parser parser) {
        parser.nextToken();
        Expression exp = parser.parseExpression(Precedence.LOWEST);
        if (!parser.expectPeek(TokenType.RPAREN)) {
            return null;
        }
        return exp;
    }

}
