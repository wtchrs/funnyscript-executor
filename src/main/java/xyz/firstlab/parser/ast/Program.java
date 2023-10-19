package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Program implements Node {

    private final List<Expression> expressions = new ArrayList<>();

    public void append(Expression expr) {
        expressions.add(expr);
    }

    public List<Expression> getExpressions() {
        return Collections.unmodifiableList(expressions);
    }

    @Override
    public Token token() {
        if (expressions.isEmpty()) {
            return null;
        }
        return expressions.get(0).token();
    }

    @Override
    public String string() {
        StringBuilder sb = new StringBuilder();
        for (Expression exp : expressions) {
            sb.append(exp.string()).append('\n');
        }
        return sb.toString();
    }

}
