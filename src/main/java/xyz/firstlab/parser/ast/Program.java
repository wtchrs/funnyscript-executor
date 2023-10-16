package xyz.firstlab.parser.ast;

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
    public String tokenLiteral() {
        if (expressions.isEmpty()) {
            return "";
        }
        return expressions.get(0).string();
    }

    @Override
    public String string() {
        StringBuilder sb = new StringBuilder();
        for (Expression expr : expressions) {
            sb.append(expr.string()).append('\n');
        }
        return sb.toString();
    }
}
