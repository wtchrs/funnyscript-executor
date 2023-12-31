package xyz.firstlab.evaluator;

import xyz.firstlab.evaluator.object.ErrorValue;
import xyz.firstlab.evaluator.object.Value;
import xyz.firstlab.ast.Node;

public abstract class Evaluator {

    public static Value evaluate(Node node) {
        Environment env = Environment.create();
        return evaluate(node, env);
    }

    public static Value evaluate(Node node, Environment env) {
        try {
            return node.evaluate(env);
        } catch (EvaluatingErrorException e) {
            return new ErrorValue(e.getMessage());
        }
    }

}
