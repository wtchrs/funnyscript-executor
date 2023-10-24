package xyz.firstlab.parser.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.EvaluatingErrorException;
import xyz.firstlab.evaluator.object.NumberValue;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.evaluator.object.ValueType;
import xyz.firstlab.token.Token;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InfixExpression extends Expression {

    private final String operator;

    private final Expression left;

    private final Expression right;

    public InfixExpression(Token token, String operator, Expression left, Expression right) {
        super(token);
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String getOperator() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public String string() {
        return String.format("(%s %s %s)", left.string(), operator, right.string());
    }

    @Override
    public Value evaluate(Environment env) {
        Value leftValue = left.evaluate(env);
        Value rightValue = right.evaluate(env);

        if (leftValue.type() == ValueType.NUMBER && rightValue.type() == ValueType.NUMBER) {
            return evaluateNumberValue(leftValue, rightValue);
        }

        if (leftValue.type() == ValueType.BOOLEAN && rightValue.type() == ValueType.BOOLEAN) {
            throw new UnsupportedOperationException("Not implemented.");
        }

        throw new UnsupportedOperationException("Not implemented.");
    }

    private Value evaluateNumberValue(Value left, Value right) {
        BigDecimal leftValue = ((NumberValue) left).getValue();
        BigDecimal rightValue = ((NumberValue) right).getValue();

        return switch (operator) {
            case "+" -> new NumberValue(leftValue.add(rightValue));
            case "-" -> new NumberValue(leftValue.subtract(rightValue));
            case "*" -> new NumberValue(leftValue.multiply(rightValue));
            case "/" -> new NumberValue(leftValue.divide(rightValue, 50, RoundingMode.HALF_UP));
            case "^" -> {
                double leftDouble = leftValue.doubleValue();
                double rightDouble = rightValue.doubleValue();
                yield new NumberValue(BigDecimal.valueOf(Math.pow(leftDouble, rightDouble)));
            }
            default -> {
                String message = String.format("Unknown operator: %s", string());
                throw new EvaluatingErrorException(token(), message);
            }
        };
    }

}
