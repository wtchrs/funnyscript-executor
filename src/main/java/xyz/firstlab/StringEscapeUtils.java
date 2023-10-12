package xyz.firstlab;

public final class StringEscapeUtils {

    private StringEscapeUtils() {
    }

    public static String escapeString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
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
