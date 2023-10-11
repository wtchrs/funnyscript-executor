package xyz.firstlab.token;

import lombok.Getter;

@Getter
public enum TokenType {

    ILLEGAL("ILLEGAL"),
    EOF("EOF"),
    NEWLINE("NEWLINE"),
    IDENT("IDENT"),
    NUMBER("NUMBER"),
    STRING("STRING"),
    ASSIGN("="),
    PLUS("+"),
    MINUS("-"),
    ASTERISK("*"),
    SLASH("/"),
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
    NOT_EQ("!=");

    private final String value;

    TokenType(String value) {
        this.value = value;
    }

}
