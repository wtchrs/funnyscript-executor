package xyz.firstlab.parser;

import xyz.firstlab.token.TokenType;

import java.util.Map;

public enum Precedence {

    LOWEST(0),
    EQUALS(1),
    LESS_GREATER(2),
    SUM(3),
    PRODUCT(4),
    POWER(5),
    PREFIX(6),
    CALL(7),
    INDEX(8),
    ;

    private static final Map<TokenType, Precedence> TOKEN_TYPE_PRECEDENCE_MAP = Map.ofEntries(
            Map.entry(TokenType.EQ, EQUALS),
            Map.entry(TokenType.NOT_EQ, EQUALS),
            Map.entry(TokenType.LT, LESS_GREATER),
            Map.entry(TokenType.GT, LESS_GREATER),
            Map.entry(TokenType.LTE, LESS_GREATER),
            Map.entry(TokenType.GTE, LESS_GREATER),
            Map.entry(TokenType.PLUS, SUM),
            Map.entry(TokenType.MINUS, SUM),
            Map.entry(TokenType.ASTERISK, PRODUCT),
            Map.entry(TokenType.SLASH, PRODUCT),
            Map.entry(TokenType.CARET, POWER),
            Map.entry(TokenType.LPAREN, CALL)
    );

    public static Precedence getPrecedence(TokenType type) {
        return TOKEN_TYPE_PRECEDENCE_MAP.getOrDefault(type, LOWEST);
    }

    private final int value;

    Precedence(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
