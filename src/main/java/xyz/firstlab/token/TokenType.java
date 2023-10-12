package xyz.firstlab.token;

import lombok.Getter;

import java.util.Map;

@Getter
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
    BANG("!"),
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
    CASE("CASE"),
    DEFAULT("DEFAULT"),
    ;

    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    private static final Map<String, TokenType> keywords = Map.of(
            "case", TokenType.CASE,
            "default", TokenType.DEFAULT
    );

    public static TokenType lookupIdent(CharSequence ident) {
        TokenType type = keywords.get(ident.toString());
        if (type != null) {
            return type;
        }
        return TokenType.IDENT;
    }

}
