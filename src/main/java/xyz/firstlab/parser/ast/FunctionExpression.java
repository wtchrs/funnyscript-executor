package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

import java.util.List;

public class FunctionExpression extends Expression {

    private final Expression function;

    private final List<Expression> arguments;

    public FunctionExpression(Token token, Expression function, List<Expression> arguments) {
        super(token);
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public String string() {
        List<String> argumentStrings = arguments.stream().map(Expression::string).toList();
        String joinedString = String.join(", ", argumentStrings);
        return String.format("%s(%s)", function.string(), joinedString);
    }
}
