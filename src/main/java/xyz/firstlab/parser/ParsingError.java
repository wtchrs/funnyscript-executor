package xyz.firstlab.parser;

public class ParsingError {

    private final int lineNumber, columnNumber;

    private final String message;

    public ParsingError(int lineNumber, int columnNumber, String message) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.message = message;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("Parsing Error: %d:%d: %s", lineNumber, columnNumber, message);
    }

}
