package xyz.firstlab.parser.ast;

public interface Node {

    /**
     * Return the first token's literal of the node.
     *
     * @return first token's literal.
     */
    String tokenLiteral();

    /**
     * Return a string that describe the state of ast node.
     *
     * @return description of ast node.
     */
    String string();

}
