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

        int lineNumber = this.lineNumber;
        int columnNumber = this.columnNumber;

        return switch (ch) {
            case '=' -> readTwoCharToken(TokenType.ASSIGN, TokenType.EQ, lineNumber, columnNumber, '=');
            case '+' -> readSingleCharToken(TokenType.PLUS, lineNumber, columnNumber);
            case '-' -> readTwoCharToken(TokenType.MINUS, TokenType.ARROW, lineNumber, columnNumber, '>');
            case '*' -> readSingleCharToken(TokenType.ASTERISK, lineNumber, columnNumber);
            case '/' -> readTwoCharToken(TokenType.SLASH, TokenType.NOT_EQ, lineNumber, columnNumber, '=');
            case '^' -> readSingleCharToken(TokenType.CARET, lineNumber, columnNumber);
            case '<' -> readTwoCharToken(TokenType.LT, TokenType.LTE, lineNumber, columnNumber, '=');
            case '>' -> readTwoCharToken(TokenType.GT, TokenType.GTE, lineNumber, columnNumber, '=');
            case '(' -> readSingleCharToken(TokenType.LPAREN, lineNumber, columnNumber);
            case ')' -> readSingleCharToken(TokenType.RPAREN, lineNumber, columnNumber);
            case ',' -> readSingleCharToken(TokenType.COMMA, lineNumber, columnNumber);
            case '\"' -> readStringLiteralToken();
            case '\n' -> readSingleCharToken(TokenType.NEWLINE, lineNumber, columnNumber);
            case 0 -> new Token(TokenType.EOF, "", lineNumber, columnNumber);
            default -> {
                if (isLetter()) {
                    String ident = readIdentifier();
                    TokenType type = TokenType.lookupIdent(ident);
                    yield  new Token(type, ident, lineNumber, columnNumber);
                }
                if (isDigit()) {
                    String number = readNumber();
                    yield  new Token(TokenType.NUMBER, number, lineNumber, columnNumber);
                }
                readChar();
                yield new Token(TokenType.ILLEGAL, ch, lineNumber, columnNumber);
            }
        };
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

    private Token readSingleCharToken(TokenType type, int lineNumber, int columnNumber) {
        char currentChar = ch;
        readChar();
        return new Token(type, currentChar, lineNumber, columnNumber);
    }

    private Token readTwoCharToken(
            TokenType singleCharTokenType, TokenType twoCharTokenType, int lineNumber, int columnNumber, char nextChar
    ) {
        char currentChar = ch;
        if (peekChar() == nextChar) {
            readChar();
            readChar();
            return new Token(twoCharTokenType, String.valueOf(currentChar) + nextChar, lineNumber, columnNumber);
        } else {
            readChar();
            return new Token(singleCharTokenType, currentChar, lineNumber, columnNumber);
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

        if (ch == 0) {
            return new Token(
                    TokenType.ILLEGAL,
                    input.subSequence(curPos, input.length()).toString(),
                    lineNumber,
                    columnNumber
            );
        }

        int pos = position;
        readChar();

        return new Token(TokenType.STRING, input.subSequence(strPos, pos).toString(), lineNumber, columnNumber);
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
