package xyz.firstlab.parser;

import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.FunctionExpression;
import xyz.firstlab.parser.ast.Identifier;

public abstract class AssignExpressionUtils {

    public static void assertAssignable(Expression left) {
        if (left instanceof FunctionExpression funcExp) {
            assertFunctionExpressionAssignable(funcExp);
        } else if (!(left instanceof Identifier)) {
            throw new ParsingErrorException(left.token(), "Cannot assignable.");
        }
    }

    private static void assertFunctionExpressionAssignable(FunctionExpression funcExp) {
        Expression function = funcExp.getFunction();
        if (!(function instanceof Identifier)) {
            throw new ParsingErrorException(function.token(), "Not an Identifier.");
        }

        for (Expression exp : funcExp.getArguments()) {
            if (!(exp instanceof Identifier)) {
                throw new ParsingErrorException(exp.token(), "Parameter in function definition must be an Identifier.");
            }
        }
    }

}
