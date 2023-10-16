package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

import java.math.BigDecimal;

public class NumberLiteral implements Expression {

    private final Token token;

    private final BigDecimal value;

    public NumberLiteral(Token token, String value) {
        this.token = token;
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String tokenLiteral() {
        return token.getLiteral();
    }

    @Override
    public String string() {
        return value.toString();
    }

}
