package xyz.firstlab.parser;

import xyz.firstlab.parser.parselet.IdentifierParselet;
import xyz.firstlab.parser.parselet.BinaryOperatorParselet;
import xyz.firstlab.parser.parselet.NumberParselet;
import xyz.firstlab.parser.parselet.PrefixOperatorParselet;
import xyz.firstlab.token.Lexer;
import xyz.firstlab.token.TokenType;

public class DefaultParser extends Parser {

    public DefaultParser(Lexer lexer) {
        super(lexer);

        // register PrefixParselet
        register(TokenType.NUMBER, new NumberParselet());
        register(TokenType.IDENT, new IdentifierParselet());
        register(TokenType.PLUS, new PrefixOperatorParselet());
        register(TokenType.MINUS, new PrefixOperatorParselet());

        // register InfixParselet
        register(TokenType.PLUS, new BinaryOperatorParselet(Precedence.SUM));
        register(TokenType.MINUS, new BinaryOperatorParselet(Precedence.SUM));
        register(TokenType.ASTERISK, new BinaryOperatorParselet(Precedence.PRODUCT));
        register(TokenType.SLASH, new BinaryOperatorParselet(Precedence.PRODUCT));
        register(TokenType.CARET, new BinaryOperatorParselet(Precedence.POWER));
    }

}
