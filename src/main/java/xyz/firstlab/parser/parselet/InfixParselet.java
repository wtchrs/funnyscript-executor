package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.Precedence;
import xyz.firstlab.parser.ast.Expression;

public interface InfixParselet {

    Expression parse(Parser parser, Expression left);

    Precedence getPrecedence();

}
