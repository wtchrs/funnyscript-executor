package xyz.firstlab.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.EvaluatorUtils;
import xyz.firstlab.evaluator.EvaluatingErrorException;
import xyz.firstlab.evaluator.object.NumberValue;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.evaluator.object.ValueType;
import xyz.firstlab.lexer.Token;

public class PrefixExpression extends Expression {

    private final String operator;

    private final Expression right;

    public PrefixExpression(Token token, String operator, Expression right) {
        super(token);
        this.operator = operator;
        this.right = right;
    }

    public String getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public String string() {
        if (operator.length() > 1) {
            return String.format("(%s %s)", operator, right.string());
        }
        return String.format("(%s%s)", operator, right.string());
    }

    @Override
    public Value evaluate(Environment env) {
        final Value evaluated = right.evaluate(env);

        return switch (operator) {
            case "+" -> {
                EvaluatorUtils.assertType(evaluated, token(), ValueType.NUMBER);
                yield evaluated;
            }
            case "-" -> {
                EvaluatorUtils.assertType(evaluated, token(), ValueType.NUMBER);
                yield new NumberValue(((NumberValue)evaluated).getValue().negate());
            }
            case "not" -> {
                EvaluatorUtils.assertType(evaluated, token(), ValueType.BOOLEAN);
                throw new UnsupportedOperationException("Not implemented.");
            }
            default -> {
                String message = String.format("Unknown operator: %s %s", operator, evaluated.type());
                throw new EvaluatingErrorException(token(), message);
            }
        };
    }

}
