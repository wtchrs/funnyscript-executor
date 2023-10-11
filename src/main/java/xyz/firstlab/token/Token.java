package xyz.firstlab.token;

import lombok.Getter;

@Getter
public class Token {

    private final TokenType type;

    private final String literal;

    private final int lineNumber;

    private final int columnNumber;

    public Token(TokenType type, String literal, int lineNumber, int columnNumber) {
        this.type = type;
        this.literal = literal;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

}
