package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.Precedence;
import xyz.firstlab.ast.BooleanLiteral;
import xyz.firstlab.ast.CaseExpression;
import xyz.firstlab.ast.Expression;
import xyz.firstlab.lexer.Token;
import xyz.firstlab.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class CaseExpressionParselet implements PrefixParselet {

    @Override
    public Expression parse(Parser parser) {
        List<CaseExpression.Case> cases = new ArrayList<>();

        Token curToken = parser.getCurToken();

        while (!parser.peekTokenIs(TokenType.EOF)) {
            parser.nextToken();

            Expression cond;

            if (parser.curTokenIs(TokenType.DEFAULT)) {
                cond = new BooleanLiteral(parser.getCurToken(), true);
            } else {
                cond = parser.parseExpression(Precedence.LOWEST);
            }

            parser.assertPeekIs(TokenType.ARROW);
            parser.nextToken();

            Expression exp = parser.parseExpression(Precedence.LOWEST);
            cases.add(new CaseExpression.Case(cond, exp));

            if (!parser.peekTokenIs(TokenType.COMMA)) {
                break;
            }

            parser.nextToken();
        }

        return new CaseExpression(curToken, cases);
    }

}
