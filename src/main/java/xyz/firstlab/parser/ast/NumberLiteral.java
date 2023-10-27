package xyz.firstlab.parser.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.object.NumberValue;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.lexer.Token;

import java.math.BigDecimal;

public class NumberLiteral extends Expression {

    private final BigDecimal value;

    public NumberLiteral(Token token, String value) {
        super(token);
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String string() {
        return value.toString();
    }

    @Override
    public Value evaluate(Environment env) {
        return new NumberValue(value);
    }

}
