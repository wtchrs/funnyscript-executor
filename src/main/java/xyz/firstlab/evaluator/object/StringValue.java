package xyz.firstlab.evaluator.object;

public class StringValue implements Value {

    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public ValueType type() {
        return ValueType.STRING;
    }

    @Override
    public String inspect() {
        return value;
    }
}
