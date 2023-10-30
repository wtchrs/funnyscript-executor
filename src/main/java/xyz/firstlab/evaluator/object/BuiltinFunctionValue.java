package xyz.firstlab.evaluator.object;

import xyz.firstlab.ast.Expression;

@FunctionalInterface
public interface BuiltinFunctionValue extends Value {

    Value apply(Expression expression, Value... arguments);

    @Override
    default ValueType type() {
        return ValueType.BUILTIN_FUNCTION;
    }

    @Override
    default String inspect() {
        return "<BUILT-IN FUNCTION>";
    }

}
