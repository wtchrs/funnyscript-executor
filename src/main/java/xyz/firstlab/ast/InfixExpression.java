package xyz.firstlab.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.EvaluatingErrorException;
import xyz.firstlab.evaluator.object.*;
import xyz.firstlab.lexer.Token;

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
            return evaluateBooleanValue(leftValue, rightValue);
        }

        if (leftValue.type() == ValueType.STRING && rightValue.type() == ValueType.STRING) {
            return evaluateStringValue(leftValue, rightValue);
        }

        String message = String.format("Unknown operator: %s %s %s", leftValue.type(), operator, rightValue.type());
        throw new EvaluatingErrorException(token(), message);
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
            case "==" -> new BooleanValue(leftValue.compareTo(rightValue) == 0);
            case "/=" -> new BooleanValue(leftValue.compareTo(rightValue) != 0);
            case "<" -> new BooleanValue(leftValue.compareTo(rightValue) < 0);
            case "<=" -> new BooleanValue(leftValue.compareTo(rightValue) <= 0);
            case ">" -> new BooleanValue(leftValue.compareTo(rightValue) > 0);
            case ">=" -> new BooleanValue(leftValue.compareTo(rightValue) >= 0);
            default -> {
                String message = String.format("Unknown operator: %s %s %s", left.type(), operator, right.type());
                throw new EvaluatingErrorException(token(), message);
            }
        };
    }

    private Value evaluateBooleanValue(Value left, Value right) {
        boolean leftValue = ((BooleanValue) left).getValue();
        boolean rightValue = ((BooleanValue) right).getValue();

        return switch (operator) {
            case "==" -> new BooleanValue(leftValue == rightValue);
            case "/=" -> new BooleanValue(leftValue != rightValue);
            case "and" -> new BooleanValue(leftValue && rightValue);
            case "or" -> new BooleanValue(leftValue || rightValue);
            default -> {
                String message = String.format("Unknown operator: %s %s %s", left.type(), operator, right.type());
                throw new EvaluatingErrorException(token(), message);
            }
        };
    }

    private Value evaluateStringValue(Value left, Value right) {
        String leftValue = ((StringValue) left).getValue();
        String rightValue = ((StringValue) right).getValue();

        if (operator.equals("+")) {
            return new StringValue(leftValue + rightValue);
        }

        String message = String.format("Unknown operator: %s %s %s", left.type(), operator, right.type());
        throw new EvaluatingErrorException(token(), message);
    }

}
