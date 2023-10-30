package xyz.firstlab.evaluator.object;

import java.util.Map;

public enum ValueType {

    NUMBER,
    STRING,
    BOOLEAN,
    NULL,
    FUNCTION,
    BUILTIN_FUNCTION,
    ERROR,
    ;

    private static final Map<Class<? extends Value>, ValueType> classToType = Map.of(
            NumberValue.class, NUMBER,
            StringValue.class, STRING,
            BooleanValue.class, BOOLEAN,
            FunctionValue.class, FUNCTION,
            BuiltinFunctionValue.class, BUILTIN_FUNCTION,
            ErrorValue.class, ERROR
    );

    public static ValueType getType(Class<? extends Value> clazz) {
        return classToType.get(clazz);
    }

}
