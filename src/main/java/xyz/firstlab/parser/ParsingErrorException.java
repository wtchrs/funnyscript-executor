package xyz.firstlab.parser;

import xyz.firstlab.token.Token;

public class ParsingErrorException extends RuntimeException {

    private final int lineNumber, columnNumber;

    public ParsingErrorException(Token token, String message) {
        super(message);
        this.lineNumber = token.getLineNumber();
        this.columnNumber = token.getColumnNumber();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

}
