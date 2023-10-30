package xyz.firstlab.evaluator.object;

import xyz.firstlab.ast.Expression;
import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.EvaluatingErrorException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

public abstract class BuiltinValues {

    public static final NumberValue PI = new NumberValue(BigDecimal.valueOf(Math.PI));

    public static final NumberValue E = new NumberValue(BigDecimal.valueOf(Math.E));

    public static void initGlobalEnv(Environment env) {
        // Built-in variables
        env.set("PI", PI);
        env.set("E", E);

        // Built-in functions
        env.set("abs", (BuiltinFunctionValue) BuiltinValues::abs);
        env.set("sqrt", (BuiltinFunctionValue) BuiltinValues::sqrt);
        env.set("min", (BuiltinFunctionValue) BuiltinValues::min);
        env.set("max", (BuiltinFunctionValue) BuiltinValues::max);
    }

    private static Value abs(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        NumberValue number = assertType(exp, args[0], NumberValue.class);

        return new NumberValue(number.getValue().abs());
    }

    private static Value sqrt(Expression exp, Value... args) {
        assertLength(exp, args, 1);
        NumberValue number = assertType(exp, args[0], NumberValue.class);

        return new NumberValue(number.getValue().sqrt(NumberValue.getContext()));
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
