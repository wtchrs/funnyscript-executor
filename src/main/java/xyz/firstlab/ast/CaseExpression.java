package xyz.firstlab.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.EvaluatingErrorException;
import xyz.firstlab.evaluator.object.BooleanValue;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.evaluator.object.ValueType;
import xyz.firstlab.lexer.Token;

import java.util.List;
import java.util.stream.Collectors;

public class CaseExpression extends Expression {

    private final List<Case> cases;

    public CaseExpression(Token token, List<Case> cases) {
        super(token);
        this.cases = cases;
    }

    @Override
    public String string() {
        String casesString = cases.stream().map(Case::toString).collect(Collectors.joining(", "));
        return String.format("(case %s)", casesString);
    }

    @Override
    public Value evaluate(Environment env) {
        for (Case c : cases) {
            Value evaluatedCond = c.condition.evaluate(env);

            if (evaluatedCond.type() != ValueType.BOOLEAN) {
                String message = String.format("Condition expression '%s' is not a boolean.", c.condition.string());
                throw new EvaluatingErrorException(token(), message);
            }

            if (((BooleanValue) evaluatedCond).getValue()) {
                return c.expression.evaluate(env);
            }
        }

        throw new EvaluatingErrorException(token(), "No case matched.");
    }

    public static class Case {

        private final Expression condition, expression;

        public Case(Expression condition, Expression expression) {
            this.condition = condition;
            this.expression = expression;
        }

        public Expression getCondition() {
            return condition;
        }

        public Expression getExpression() {
            return expression;
        }

        @Override
        public String toString() {
            return String.format("(%s -> %s)", condition.string(), expression.string());
        }
    }

}
