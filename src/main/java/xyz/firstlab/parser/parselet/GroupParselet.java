package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.Precedence;
import xyz.firstlab.ast.Expression;
import xyz.firstlab.lexer.TokenType;

public class GroupParselet implements PrefixParselet {

    @Override
    public Expression parse(Parser parser) {
        parser.nextToken();
        Expression exp = parser.parseExpression(Precedence.LOWEST);
        parser.assertPeekIs(TokenType.RPAREN);
        return exp;
    }

}
