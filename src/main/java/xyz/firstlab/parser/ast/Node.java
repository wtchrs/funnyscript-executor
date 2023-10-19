package xyz.firstlab.parser.ast;

import xyz.firstlab.token.Token;

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

}
