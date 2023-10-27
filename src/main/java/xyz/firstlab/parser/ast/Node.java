package xyz.firstlab.parser.ast;

import xyz.firstlab.evaluator.Environment;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.lexer.Token;

public interface Node {

    /**
     * Return the token of the node.
     *
     * @return first token's literal.
     */
    Token token();

    /**
     * Return a string that describe the state of ast node.
     *
     * @return description of ast node.
     */
    String string();

    Value evaluate(Environment env);

}
