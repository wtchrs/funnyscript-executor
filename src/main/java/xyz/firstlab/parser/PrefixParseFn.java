package xyz.firstlab.parser;

import xyz.firstlab.parser.ast.Expression;

@FunctionalInterface
public interface PrefixParseFn {

    Expression parse();

}
