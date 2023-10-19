package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.ParsingError;
import xyz.firstlab.parser.Precedence;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.FunctionExpression;
import xyz.firstlab.parser.ast.Identifier;
import xyz.firstlab.parser.ast.InfixExpression;
import xyz.firstlab.token.Token;

public class AssignExpressionParselet implements InfixParselet {

    @Override
    public Expression parse(Parser parser, Expression left) {
        // left must be a function or identifier.
        if (!expectAssignable(parser, left)) {
            return null;
        }

        Token curToken = parser.getCurToken();
        parser.nextToken();
        Expression right = parser.parseExpression(getPrecedence().getValue() - 1);

        return new InfixExpression(curToken, curToken.getLiteral(), left, right);
    }

    @Override
    public Precedence getPrecedence() {
        return Precedence.ASSIGN;
    }

    private boolean expectAssignable(Parser parser, Expression left) {
        if (left instanceof FunctionExpression funcExp) {
            return expectFunctionExpressionAssignable(parser, funcExp);
        } else if (!(left instanceof Identifier)) {
            parser.appendError(new ParsingError(
                    left.token().getLineNumber(), left.token().getColumnNumber(), "Cannot assignable."
            ));
            return false;
        }

        return true;
    }

    private boolean expectFunctionExpressionAssignable(Parser parser, FunctionExpression funcExp) {
        Expression function = funcExp.getFunction();
        if (!(function instanceof Identifier)) {
            Token token = function.token();
            parser.appendError(new ParsingError(
                    token.getLineNumber(), token.getColumnNumber(), "Not an Identifier."
            ));
            return false;
        }

        for (Expression exp : funcExp.getArguments()) {
            if (!(exp instanceof Identifier)) {
                Token token = exp.token();
                parser.appendError(new ParsingError(
                        token.getLineNumber(), token.getColumnNumber(),
                        "Parameter in function definition must be an Identifier."
                ));
                return false;
            }
        }

        return true;
    }

}
