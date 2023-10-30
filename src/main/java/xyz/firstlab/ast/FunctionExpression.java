package xyz.firstlab.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.EvaluatingErrorException;
import xyz.firstlab.evaluator.object.FunctionValue;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.evaluator.object.ValueType;
import xyz.firstlab.lexer.Token;

import java.util.Collections;
import java.util.List;

public class FunctionExpression extends Expression {

    private final Expression function;

    private final List<Expression> arguments;

    public FunctionExpression(Token token, Expression function, List<Expression> arguments) {
        super(token);
        this.function = function;
        this.arguments = arguments;
    }

    public Expression getFunction() {
        return function;
    }

    public List<Expression> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public String string() {
        List<String> argumentStrings = arguments.stream().map(Expression::string).toList();
        String joinedString = String.join(", ", argumentStrings);
        return String.format("%s(%s)", function.string(), joinedString);
    }

    @Override
    public Value evaluate(Environment env) {
        Value value = function.evaluate(env);

        if (value.type() != ValueType.FUNCTION) {
            String message = String.format("Not a function: %s", function.string());
            throw new EvaluatingErrorException(token(), message);
        }

        FunctionValue func = (FunctionValue) value;
        List<Identifier> parameters = func.getParameters();
        Environment enclosedEnv = func.getEnclosedEnv();

        for (int i = 0; i < parameters.size() && i < arguments.size(); i++) {
            enclosedEnv.set(parameters.get(i).getValue(), arguments.get(i).evaluate(env));
        }

        if (parameters.size() <= arguments.size()) {
            return func.getBody().evaluate(enclosedEnv);
        }

        // If passed arguments is less than function parameters, curried function returned.
        List<Identifier> newParams = parameters.subList(arguments.size(), parameters.size());
        return new FunctionValue(newParams, func.getBody(), enclosedEnv);
    }

}
