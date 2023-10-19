package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.Precedence;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.FunctionExpression;
import xyz.firstlab.token.Token;
import xyz.firstlab.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class FunctionParselet implements InfixParselet {

    @Override
    public Expression parse(Parser parser, Expression left) {
        Token curToken = parser.getCurToken();
        List<Expression> params = new ArrayList<>();

        while (!parser.peekTokenIs(TokenType.EOF) && !parser.peekTokenIs(TokenType.RPAREN)) {
            parser.nextToken();

            Expression exp = parser.parseExpression(Precedence.LOWEST.getValue());
            params.add(exp);

            if (parser.peekTokenIs(TokenType.COMMA)) {
                parser.nextToken();
            }
        }

        if (!parser.expectPeek(TokenType.RPAREN)) {
            return null;
        }

        return new FunctionExpression(curToken, left, params);
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.FUNCTION;
    }

}
