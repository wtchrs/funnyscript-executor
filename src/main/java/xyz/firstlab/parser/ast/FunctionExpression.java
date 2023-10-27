package xyz.firstlab.parser.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.token.Token;

import java.util.Collections;
import java.util.List;

public class FunctionExpression extends Expression {

    private final Expression functionIdent;

    private final List<Expression> arguments;

    public FunctionExpression(Token token, Expression functionIdent, List<Expression> arguments) {
        super(token);
        this.functionIdent = functionIdent;
        this.arguments = arguments;
    }

    public Expression getFunctionIdent() {
        return functionIdent;
    }

    public List<Expression> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public String string() {
        List<String> argumentStrings = arguments.stream().map(Expression::string).toList();
        String joinedString = String.join(", ", argumentStrings);
        return String.format("%s(%s)", functionIdent.string(), joinedString);
    }

    @Override
    public Value evaluate(Environment env) {
        throw new UnsupportedOperationException("Not implemented.");
    }

}
