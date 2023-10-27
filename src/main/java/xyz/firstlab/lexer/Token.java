package xyz.firstlab.lexer;

import java.util.Objects;

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

    public TokenType getType() {
        return type;
    }

    public String getLiteral() {
        return literal;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public String toString() {
        return String.format(
                "Token(type=%s, literal=\"%s\", lineNumber=%d, columnNumber=%d)",
                type.toString(), StringEscapeUtils.escapeString(literal), lineNumber, columnNumber
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return getLineNumber() == token.getLineNumber() && getColumnNumber() == token.getColumnNumber() &&
                getType() == token.getType() && Objects.equals(getLiteral(), token.getLiteral());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getLiteral(), getLineNumber(), getColumnNumber());
    }

}
