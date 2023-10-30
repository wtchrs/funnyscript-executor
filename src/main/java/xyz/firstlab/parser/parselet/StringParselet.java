package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.ast.Expression;
import xyz.firstlab.ast.StringLiteral;
import xyz.firstlab.lexer.Token;

public class StringParselet implements PrefixParselet {
    @Override
    public Expression parse(Parser parser) {
        Token curToken = parser.getCurToken();
        return new StringLiteral(curToken, curToken.getLiteral());
    }
}
