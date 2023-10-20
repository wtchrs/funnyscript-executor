package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

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
