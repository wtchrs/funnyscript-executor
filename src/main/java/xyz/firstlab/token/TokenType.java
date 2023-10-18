package xyz.firstlab.token;

import java.util.Map;

public enum TokenType {

    ILLEGAL("ILLEGAL"),
    EOF("EOF"),
    NEWLINE("\\n"),
    IDENT("IDENT"),
    NUMBER("NUMBER"),
    STRING("STRING"),
    ASSIGN("="),
    PLUS("+"),
    MINUS("-"),
    ASTERISK("*"),
    SLASH("/"),
    CARET("^"),
//    BANG("!"),
    NOT("NOT"),
    LT("<"),
    LTE("<="),
    GT(">"),
    GTE(">="),
    COMMA(","),
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    EQ("=="),
    NOT_EQ("/="),
    ARROW("->"),
    TRUE("TRUE"),
    FALSE("FALSE"),
    CASE("CASE"),
    DEFAULT("DEFAULT"),
    ;

    private final String value;

    private static final Map<String, TokenType> keywords = Map.of(
            "not", TokenType.NOT,
            "true", TokenType.TRUE,
            "false", TokenType.FALSE,
            "case", TokenType.CASE,
            "default", TokenType.DEFAULT
    );

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static TokenType lookupIdent(CharSequence ident) {
        TokenType type = keywords.get(ident.toString());
        if (type != null) {
            return type;
        }
        return TokenType.IDENT;
    }

}
