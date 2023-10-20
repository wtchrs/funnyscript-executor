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
        register(TokenType.CASE, new CaseExpressionParselet());

        // register InfixParselet
        register(TokenType.ASSIGN, new AssignExpressionParselet());
        register(TokenType.LPAREN, new FunctionParselet());
        infixLeft(TokenType.EQ, Precedence.EQUALS);
        infixLeft(TokenType.NOT_EQ, Precedence.EQUALS);
        infixLeft(TokenType.LT, Precedence.LESS_GREATER);
        infixLeft(TokenType.LTE, Precedence.LESS_GREATER);
        infixLeft(TokenType.GT, Precedence.LESS_GREATER);
        infixLeft(TokenType.GTE, Precedence.LESS_GREATER);
        infixLeft(TokenType.PLUS, Precedence.SUM);
        infixLeft(TokenType.MINUS, Precedence.SUM);
        infixLeft(TokenType.ASTERISK, Precedence.PRODUCT);
        infixLeft(TokenType.SLASH, Precedence.PRODUCT);
        infixRight(TokenType.CARET, Precedence.EXPONENT);
    }

    private void infixLeft(TokenType type, Precedence precedence) {
        register(type, new BinaryOperatorParselet(precedence, false));
    }

    private void infixRight(TokenType type, Precedence precedence) {
        register(type, new BinaryOperatorParselet(precedence, true));
    }

}
