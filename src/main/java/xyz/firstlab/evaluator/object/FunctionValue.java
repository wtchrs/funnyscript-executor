package xyz.firstlab.evaluator.object;

import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.Identifier;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionValue implements Value {

    private final List<Identifier> parameters;

    private final Expression body;

    public FunctionValue(List<Identifier> parameters, Expression body) {
        this.parameters = parameters;
        this.body = body;
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
