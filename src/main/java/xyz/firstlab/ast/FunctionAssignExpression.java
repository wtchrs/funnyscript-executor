package xyz.firstlab.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.EvaluatingErrorException;
import xyz.firstlab.evaluator.object.FunctionValue;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.lexer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionAssignExpression extends Expression {

    private final Expression functionIdent;

    private final List<Identifier> parameters;

    private final Expression right;

    public FunctionAssignExpression(Token token, FunctionExpression left, Expression right) {
        super(token);
        this.functionIdent = left.getFunction();
        this.parameters = paramsToIdents(left.getArguments());
        this.right = right;
    }

    @Override
    public String string() {
        String paramString = parameters.stream().map(Identifier::getValue).collect(Collectors.joining(", "));
        return String.format("(%s(%s) = %s)", functionIdent.string(), paramString, right.string());
    }

    @Override
    public Value evaluate(Environment env) {
        FunctionValue function = new FunctionValue(parameters, right, env);
        env.set(functionIdent.string(), function);
        return env.get(functionIdent.string());
    }

    private static List<Identifier> paramsToIdents(List<Expression> params) {
        List<Identifier> idents = new ArrayList<>(params.size());

        for (Expression param : params) {
            if (!(param instanceof Identifier)) {
                String message = String.format("'%s' is not Identifier.", param.string());
                throw new EvaluatingErrorException(param.token(), message);
            }

            idents.add((Identifier) param);
        }

        return idents;
    }

}
