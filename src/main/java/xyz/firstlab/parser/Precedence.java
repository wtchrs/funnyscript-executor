package xyz.firstlab.parser;

public enum Precedence {

    LOWEST(0),
    ASSIGN(LOWEST),
    OR(ASSIGN),
    AND(OR),
    EQUALS(AND),
    LESS_GREATER(EQUALS),
    SUM(LESS_GREATER),
    PRODUCT(SUM),
    EXPONENT(PRODUCT),
    PREFIX(EXPONENT),
    FUNCTION(PREFIX),
//    INDEX(FUNCTION),
    ;

    private final int value;

    Precedence(int value) {
        this.value = value;
    }

    Precedence(Precedence prev) {
        this.value = prev.value + 1;
    }

    public int getValue() {
        return value;
    }

}
