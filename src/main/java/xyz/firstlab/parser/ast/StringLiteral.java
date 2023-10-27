package xyz.firstlab.parser.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.object.StringValue;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.lexer.Token;

public class StringLiteral extends Expression {

    private final String value;

    public StringLiteral(Token token, String value) {
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
        return new StringValue(value);
    }

}
