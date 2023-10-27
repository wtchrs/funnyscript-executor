package xyz.firstlab.evaluator;

import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.evaluator.object.ValueType;
import xyz.firstlab.lexer.Token;

public abstract class EvaluatorUtils {

    public static void assertType(Value value, Token token, ValueType... types) {
        for (ValueType type : types) {
            if (value.type() == type) {
                return;
            }
        }
        String message = String.format("Wrong value type: %s", value.type());
        throw new EvaluatingErrorException(token, message);
    }

}
