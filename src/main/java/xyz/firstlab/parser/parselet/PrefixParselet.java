package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.ast.Expression;

public interface PrefixParselet {
    Expression parse(Parser parser);
}
