package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

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

}
