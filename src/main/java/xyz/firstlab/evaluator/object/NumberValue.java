package xyz.firstlab.evaluator.object;

import java.math.BigDecimal;

public class NumberValue implements Value {

    private final BigDecimal value;

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
