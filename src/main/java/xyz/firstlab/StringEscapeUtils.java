package xyz.firstlab;

public final class StringEscapeUtils {

    private StringEscapeUtils() {
        throw new UnsupportedOperationException("StringEscapeUtils: Constructor calls are prohibited.");
    }

    public static String escapeString(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\"' -> sb.append("\\\"");
                case '\'' -> sb.append("\\'");
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }

}
