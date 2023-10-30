package xyz.firstlab.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.EvaluatingErrorException;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.lexer.Token;

public class Identifier extends Expression {

    private final String value;

    public Identifier(Token token, String value) {
        super(token);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String string() {
        return value;
    }

    @Override
    public Value evaluate(Environment env) {
        Value value = env.get(this.value);

        if (value == null) {
            String message = String.format("Not exist variable: '%s'", this.value);
            throw new EvaluatingErrorException(token(), message);
        }

        return value;
    }

}
