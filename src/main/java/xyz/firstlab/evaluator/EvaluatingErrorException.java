package xyz.firstlab.evaluator;

import xyz.firstlab.token.Token;

public class EvaluatingErrorException extends RuntimeException {

    private final int lineNumber, columnNumber;

    public EvaluatingErrorException(Token token, String message) {
        super(message);
        this.lineNumber = token.getLineNumber();
        this.columnNumber = token.getColumnNumber();
    }

    public EvaluatingErrorException(Token token, String message, Throwable cause) {
        super(message, cause);
        this.lineNumber = token.getLineNumber();
        this.columnNumber = token.getColumnNumber();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public String getMessage() {
        return String.format("Error: %d:%d: %s", lineNumber, columnNumber, super.getMessage());
    }

}
