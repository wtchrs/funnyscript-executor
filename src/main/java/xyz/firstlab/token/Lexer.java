package xyz.firstlab.token;

public class Lexer {

    private final CharSequence input;

    private int position = 0;

    private int readPosition = 0;

    private int lineNumber = 1;

    private int columnNumber = 0;

    private char ch;

    public Lexer(CharSequence input) {
        this.input = input;
        readChar();
    }

    public Token nextToken() {
        skipWhitespace();

        Token token;
        int lineNumber = this.lineNumber;
        int columnNumber = this.columnNumber;

        switch (ch) {
            case '=' -> token = readTwoCharToken(TokenType.ASSIGN, TokenType.EQ, lineNumber, columnNumber, '=');
            case '+' -> token = new Token(TokenType.PLUS, ch, lineNumber, columnNumber);
            case '-' -> token = readTwoCharToken(TokenType.MINUS, TokenType.ARROW, lineNumber, columnNumber, '>');
            case '*' -> token = new Token(TokenType.ASTERISK, ch, lineNumber, columnNumber);
            case '/' -> token = readTwoCharToken(TokenType.SLASH, TokenType.NOT_EQ, lineNumber, columnNumber, '=');
            case '^' -> token = new Token(TokenType.CARET, ch, lineNumber, columnNumber);
            case '<' -> token = readTwoCharToken(TokenType.LT, TokenType.LTE, lineNumber, columnNumber, '=');
            case '>' -> token = readTwoCharToken(TokenType.GT, TokenType.GTE, lineNumber, columnNumber, '=');
            case '(' -> token = new Token(TokenType.LPAREN, ch, lineNumber, columnNumber);
            case ')' -> token = new Token(TokenType.RPAREN, ch, lineNumber, columnNumber);
            case ',' -> token = new Token(TokenType.COMMA, ch, lineNumber, columnNumber);
            case '\"' -> token = readStringLiteralToken();
            case '\n' -> token = new Token(TokenType.NEWLINE, ch, lineNumber, columnNumber);
            case 0 -> token = new Token(TokenType.EOF, "", lineNumber, columnNumber);
            default -> {
                if (isLetter()) {
                    String ident = readIdentifier();
                    TokenType type = TokenType.lookupIdent(ident);
                    return new Token(type, ident, lineNumber, columnNumber);
                }
                if (isDigit()) {
                    String number = readNumber();
                    return new Token(TokenType.NUMBER, number, lineNumber, columnNumber);
                }
                return new Token(TokenType.ILLEGAL, ch, lineNumber, columnNumber);
            }
        }

        readChar();

        return token;
    }

    private void readChar() {
        if (ch == '\n') {
            lineNumber++;
            columnNumber = 1;
        } else {
            columnNumber++;
        }

        if (readPosition >= input.length()) {
            ch = 0;
        } else {
            ch = input.charAt(readPosition);
        }

        position = readPosition;
        readPosition++;
    }

    private char peekChar() {
        if (readPosition >= input.length()) {
            return 0;
        }
        return input.charAt(readPosition);
    }

    private Token readTwoCharToken(
            TokenType singleCharTokenType, TokenType twoCharTokenType, int lineNumber, int columnNumber, char nextChar
    ) {
        if (peekChar() == nextChar) {
            char currentChar = ch;
            readChar();
            return new Token(twoCharTokenType, String.valueOf(currentChar) + ch, lineNumber, columnNumber);
        } else {
            return new Token(singleCharTokenType, ch, lineNumber, columnNumber);
        }
    }

    private String readIdentifier() {
        int curPos = position;
        while (isLetter()) {
            readChar();
        }
        return input.subSequence(curPos, position).toString();
    }

    private String readNumber() {
        int curPos = position;
        while (isDigit()) {
            readChar();
        }
        return input.subSequence(curPos, position).toString();
    }

    private Token readStringLiteralToken() {
        assert ch == '"';

        int curPos = position;
        int lineNumber = this.lineNumber;
        int columnNumber = this.columnNumber;
        readChar();
        int strPos = position;

        while (ch != '"' && ch != 0) {
            readChar();
        }

        if (peekChar() == 0) {
            return new Token(
                    TokenType.ILLEGAL,
                    input.subSequence(curPos, input.length()).toString(),
                    lineNumber,
                    columnNumber
            );
        }

        return new Token(TokenType.STRING, input.subSequence(strPos, position).toString(), lineNumber, columnNumber);
    }

    private boolean isLetter() {
        return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || ch == '_';
    }

    private boolean isDigit() {
        return '0' <= ch && ch <= '9' || ch == '.';
    }

    private void skipWhitespace() {
        while (ch == ' ' || ch == '\t' || ch == '\r') {
            readChar();
        }
    }

}
