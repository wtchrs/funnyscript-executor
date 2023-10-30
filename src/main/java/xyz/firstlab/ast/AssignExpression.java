package xyz.firstlab.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.EvaluatingErrorException;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.lexer.Token;

public class AssignExpression extends Expression {

    private final Expression left;
    private final Expression right;

    public AssignExpression(Token token, Expression left, Expression right) {
        super(token);
        this.left = left;
        this.right = right;
    }

    @Override
    public String string() {
        return String.format("(%s = %s)", left.string(), right.string());
    }

    @Override
    public Value evaluate(Environment env) {
        if (left instanceof Identifier) {
            env.set(left.string(), right.evaluate(env));
            return env.get(left.string());
        }

        String message = String.format("%s is not assignable.", left.string());
        throw new EvaluatingErrorException(token(), message);
    }
}
