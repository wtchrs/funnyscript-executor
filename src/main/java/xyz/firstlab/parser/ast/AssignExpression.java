package xyz.firstlab.parser.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.EvaluatingErrorException;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.token.Token;

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
        throw new UnsupportedOperationException("Not implemented.");
    }
}
