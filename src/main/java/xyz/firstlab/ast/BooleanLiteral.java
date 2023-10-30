package xyz.firstlab.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.object.BooleanValue;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.lexer.Token;

public class BooleanLiteral extends Expression {

    private final boolean value;

    public BooleanLiteral(Token token, boolean value) {
        super(token);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String string() {
        return Boolean.toString(value);
    }

    @Override
    public Value evaluate(Environment env) {
        return new BooleanValue(value);
    }

}
