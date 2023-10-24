package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.StringLiteral;
import xyz.firstlab.token.Token;

public class StringParselet implements PrefixParselet {
    @Override
    public Expression parse(Parser parser) {
        Token curToken = parser.getCurToken();
        return new StringLiteral(curToken, curToken.getLiteral());
    }
}
