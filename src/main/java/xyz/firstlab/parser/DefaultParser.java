package xyz.firstlab.parser;

import xyz.firstlab.parser.parselet.*;
import xyz.firstlab.token.Lexer;
import xyz.firstlab.token.TokenType;

public class DefaultParser extends Parser {

    public DefaultParser(Lexer lexer) {
        super(lexer);

        // register PrefixParselet
        register(TokenType.NUMBER, new NumberParselet());
        register(TokenType.TRUE, new BooleanParselet());
        register(TokenType.FALSE, new BooleanParselet());
        register(TokenType.IDENT, new IdentifierParselet());
        register(TokenType.PLUS, new PrefixOperatorParselet());
        register(TokenType.MINUS, new PrefixOperatorParselet());
        register(TokenType.NOT, new PrefixOperatorParselet());
        register(TokenType.LPAREN, new GroupParselet());

        // register InfixParselet
        register(TokenType.EQ, new BinaryOperatorParselet(Precedence.EQUALS));
        register(TokenType.NOT_EQ, new BinaryOperatorParselet(Precedence.EQUALS));
        register(TokenType.LT, new BinaryOperatorParselet(Precedence.LESS_GREATER));
        register(TokenType.LTE, new BinaryOperatorParselet(Precedence.LESS_GREATER));
        register(TokenType.GT, new BinaryOperatorParselet(Precedence.LESS_GREATER));
        register(TokenType.GTE, new BinaryOperatorParselet(Precedence.LESS_GREATER));
        register(TokenType.PLUS, new BinaryOperatorParselet(Precedence.SUM));
        register(TokenType.MINUS, new BinaryOperatorParselet(Precedence.SUM));
        register(TokenType.ASTERISK, new BinaryOperatorParselet(Precedence.PRODUCT));
        register(TokenType.SLASH, new BinaryOperatorParselet(Precedence.PRODUCT));
        register(TokenType.CARET, new BinaryOperatorParselet(Precedence.POWER));
        register(TokenType.LPAREN, new FunctionParselet(Precedence.FUNCTION));
    }

}
