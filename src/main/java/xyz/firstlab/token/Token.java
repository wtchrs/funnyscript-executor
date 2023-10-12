package xyz.firstlab.token;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import xyz.firstlab.StringEscapeUtils;

@Getter
@EqualsAndHashCode
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

    public Token(TokenType type, char literal, int lineNumber, int columnNumber) {
        this.type = type;
        this.literal = String.valueOf(literal);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    @Override
    public String toString() {
        return String.format(
                "Token(type=%s, literal=\"%s\", lineNumber=%d, columnNumber=%d)",
                type.toString(), StringEscapeUtils.escapeString(literal), lineNumber, columnNumber
        );
    }

}
