package xyz.firstlab.evaluator.object;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class NumberValue implements Value {

    private static final MathContext CONTEXT = new MathContext(100, RoundingMode.HALF_UP);

    private final BigDecimal value;

    public static MathContext getContext() {
        return CONTEXT;
    }

    public NumberValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public ValueType type() {
        return ValueType.NUMBER;
    }

    @Override
    public String inspect() {
        return value.toString();
    }

}
