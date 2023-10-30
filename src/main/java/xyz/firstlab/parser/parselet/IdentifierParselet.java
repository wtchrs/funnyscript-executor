package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.ast.Expression;
import xyz.firstlab.ast.Identifier;
import xyz.firstlab.lexer.Token;

public class IdentifierParselet implements PrefixParselet {

    @Override
    public Expression parse(Parser parser) {
        Token curToken = parser.getCurToken();
        return new Identifier(curToken, curToken.getLiteral());
    }

}
