package xyz.firstlab.parser;

public enum Precedence {

    LOWEST(0),
    ASSIGN(1),
    EQUALS(2),
    LESS_GREATER(3),
    SUM(4),
    PRODUCT(5),
    EXPONENT(6),
    PREFIX(7),
    FUNCTION(8),
    INDEX(9),
    ;

    private final int value;

    Precedence(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
