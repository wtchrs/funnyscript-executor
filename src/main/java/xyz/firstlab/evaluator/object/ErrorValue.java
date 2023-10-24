package xyz.firstlab.evaluator.object;

public class ErrorValue implements Value {

    private final String message;

    public ErrorValue(String message) {
        this.message = message;
    }

    @Override
    public ValueType type() {
        return ValueType.ERROR;
    }

    @Override
    public String inspect() {
        return message;
    }

}
