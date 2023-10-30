package xyz.firstlab.evaluator.object;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.firstlab.ast.Expression;
import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.EvaluatingErrorException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

public abstract class BuiltinValues {

    public static final NumberValue PI = new NumberValue(BigDecimalMath.pi(NumberValue.getMathContext()));

    public static final NumberValue E = new NumberValue(BigDecimalMath.e(NumberValue.getMathContext()));

    public static void initGlobalEnv(Environment env) {
        // Built-in variables
        env.set("PI", PI);
        env.set("E", E);

        // Built-in functions
        env.set("abs", (BuiltinFunctionValue) BuiltinValues::abs);
        env.set("sqrt", (BuiltinFunctionValue) BuiltinValues::sqrt);
        env.set("min", (BuiltinFunctionValue) BuiltinValues::min);
        env.set("max", (BuiltinFunctionValue) BuiltinValues::max);
        env.set("log", (BuiltinFunctionValue) BuiltinValues::log);
        env.set("sin", (BuiltinFunctionValue) BuiltinValues::sin);
        env.set("cos", (BuiltinFunctionValue) BuiltinValues::cos);
        env.set("tan", (BuiltinFunctionValue) BuiltinValues::tan);
        env.set("asin", (BuiltinFunctionValue) BuiltinValues::asin);
        env.set("acos", (BuiltinFunctionValue) BuiltinValues::acos);
        env.set("atan", (BuiltinFunctionValue) BuiltinValues::atan);
        env.set("atan2", (BuiltinFunctionValue) BuiltinValues::atan2);
        env.set("toDegrees", (BuiltinFunctionValue) BuiltinValues::toDegrees);
        env.set("toRadians", (BuiltinFunctionValue) BuiltinValues::toRadians);
    }

    private static Value abs(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        NumberValue number = assertType(exp, args[0], NumberValue.class);

        return new NumberValue(number.getValue().abs());
    }

    private static Value sqrt(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        NumberValue number = assertType(exp, args[0], NumberValue.class);

        return new NumberValue(BigDecimalMath.sqrt(number.getValue(), NumberValue.getMathContext()));
    }

    private static Value min(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        return Arrays.stream(args)
                .map(arg -> assertType(exp, arg, NumberValue.class))
                .min(Comparator.comparing(NumberValue::getValue))
                .orElse(null);
    }

    private static Value max(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        return Arrays.stream(args)
                .map(arg -> assertType(exp, arg, NumberValue.class))
                .max(Comparator.comparing(NumberValue::getValue))
                .orElse(null);
    }

    private static Value log(Expression exp, Value... args) {
        assertLength(exp, args, 1);

        BigDecimal x = assertType(exp, args[0], NumberValue.class).getValue();
        BigDecimal result;

        if (args.length == 1) {
            result = BigDecimalMath.log10(x, NumberValue.getMathContext());
        } else {
            BigDecimal base = x;
//            x = ((NumberValue) args[1]).getValue();
            x = assertType(exp, args[1], NumberValue.class).getValue();
            if (base.compareTo(BigDecimal.valueOf(2)) == 0) {
                result = BigDecimalMath.log2(x, NumberValue.getMathContext());
            } else {
                result = BigDecimalMath.log(x, NumberValue.getMathContext())
                        .divide(BigDecimalMath.log(base, NumberValue.getMathContext()), NumberValue.getMathContext());
            }
        }

        return new NumberValue(result);
    }

    private static Value sin(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        BigDecimal x = assertType(exp, args[0], NumberValue.class).getValue();
        return new NumberValue(BigDecimalMath.sin(x, NumberValue.getMathContext()));
    }

    private static Value cos(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        BigDecimal x = assertType(exp, args[0], NumberValue.class).getValue();
        return new NumberValue(BigDecimalMath.cos(x, NumberValue.getMathContext()));
    }

    private static Value tan(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        BigDecimal x = assertType(exp, args[0], NumberValue.class).getValue();
        return new NumberValue(BigDecimalMath.tan(x, NumberValue.getMathContext()));
    }

    private static Value asin(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        BigDecimal x = assertType(exp, args[0], NumberValue.class).getValue();
        return new NumberValue(BigDecimalMath.asin(x, NumberValue.getMathContext()));
    }

    private static Value acos(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        BigDecimal x = assertType(exp, args[0], NumberValue.class).getValue();
        return new NumberValue(BigDecimalMath.acos(x, NumberValue.getMathContext()));
    }

    private static Value atan(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        BigDecimal x = assertType(exp, args[0], NumberValue.class).getValue();
        return new NumberValue(BigDecimalMath.atan(x, NumberValue.getMathContext()));
    }

    private static Value atan2(Expression exp, Value... args) {
        assertLength(exp, args, 2);
        BigDecimal y = assertType(exp, args[0], NumberValue.class).getValue();
        BigDecimal x = assertType(exp, args[1], NumberValue.class).getValue();
        return new NumberValue(BigDecimalMath.atan2(y, x, NumberValue.getMathContext()));
    }

    private static Value toDegrees(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        BigDecimal rad = assertType(exp, args[0], NumberValue.class).getValue();
        return new NumberValue(BigDecimalMath.toDegrees(rad, NumberValue.getMathContext()));
    }

    private static Value toRadians(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        BigDecimal deg = assertType(exp, args[0], NumberValue.class).getValue();
        return new NumberValue(BigDecimalMath.toRadians(deg, NumberValue.getMathContext()));
    }

    private static void assertLength(Expression exp, Value[] args, int len) {
        if (args.length < len) {
            String message = String.format("Insufficient arguments. Least %d needed.", len);
            throw new EvaluatingErrorException(exp.token(), message);
        }
    }

    private static <T extends Value> T assertType(Expression exp, Value arg, Class<T> clazz) {
        if (!clazz.isInstance(arg)) {
            String message = String.format(
                    "'%s' type expected but '%s' passed.", ValueType.getType(clazz), arg.type().toString());
            throw new EvaluatingErrorException(exp.token(), message);
        }

        return clazz.cast(arg);
    }

}
