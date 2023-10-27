package xyz.firstlab.evaluator;

import xyz.firstlab.evaluator.object.Value;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private final Environment outer;

    private final Map<String, Value> variables = new HashMap<>();

    private Environment(Environment outer) {
        this.outer = outer;
    }

    public static Environment create() {
        return new Environment(null);
    }

    public Environment enclosed() {
        return new Environment(this);
    }

    public Value get(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }

        if (outer != null) {
            return outer.get(name);
        }

        return null;
    }

    public void set(String name, Value value) {
        variables.put(name, value);
    }

}
