package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.ast.BooleanLiteral;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.lexer.Token;
import xyz.firstlab.lexer.TokenType;

public class BooleanParselet implements PrefixParselet {

    @Override
    public Expression parse(Parser parser) {
        Token curToken = parser.getCurToken();
        return new BooleanLiteral(curToken, curToken.getType().equals(TokenType.TRUE));
    }

}
