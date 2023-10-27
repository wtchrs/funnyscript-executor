package xyz.firstlab.evaluator.object;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.Identifier;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionValue implements Value {

    private final List<Identifier> parameters;

    private final Expression body;

    private final Environment env;

    public FunctionValue(List<Identifier> parameters, Expression body, Environment env) {
        this.parameters = parameters;
        this.body = body;
        this.env = env;
    }

    public List<Identifier> getParameters() {
        return parameters;
    }

    public Expression getBody() {
        return body;
    }

    public Environment getEnclosedEnv() {
        return env.enclosed();
    }

    @Override
    public ValueType type() {
        return ValueType.FUNCTION;
    }

    @Override
    public String inspect() {
        String paramsString = parameters.stream().map(Identifier::string).collect(Collectors.joining(", "));
        return String.format("<FUNCTION(%s) = %s>", paramsString, body.string());
    }

}
