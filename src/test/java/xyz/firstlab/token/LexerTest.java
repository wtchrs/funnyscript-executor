package xyz.firstlab.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LexerTest {

    @Test
    @DisplayName("Lexer::nextToken() Number type test")
    void testNumberType() {
        String input = """
                0
                0.1
                1.
                1.0
                .1
                """;

        List<Token> expectedList = List.of(
                new Token(TokenType.NUMBER, "0", 1, 1),
                new Token(TokenType.NUMBER, "0.1", 2, 1),
                new Token(TokenType.NUMBER, "1.", 3, 1),
                new Token(TokenType.NUMBER, "1.0", 4, 1),
                new Token(TokenType.NUMBER, ".1", 5, 1),
                new Token(TokenType.EOF, "", 6, 1)
        );

        Lexer lexer = new Lexer(input);
        for (int i = 0; i < expectedList.size(); i++) {
            Token expected = expectedList.get(i);
            Token token = lexer.nextToken();

            assertThat(token)
                    .overridingErrorMessage("Test %d failed\nexpected: %s\n but was: %s", i, expected, token)
                    .isEqualTo(expected);
        }
    }

    @Test
    void testNextToken() {
        String input = """
                10 + 5
                10 - 5
                10 * 5
                10 / 5
                10 ^ 5
                ten = 10
                fifteen = ten + 5
                5 < x < 10
                x <= 10
                x > y
                x >= y
                x == y
                x /= y
                "Hello!"
                "Hello
                World!"
                "Hello World!"
                f(x) = x + 10
                abs(x) = case x < 0 -> -x, x >= 0 -> x
                fib(x) =
                    case
                    x == 0 -> 0,
                    x == 1 -> 1,
                    default -> fib(x - 2) + fib(x - 1)
                """;

        List<Token> expectedList = List.of(
                new Token(TokenType.NUMBER, "10", 1, 1), // 10 + 5
                new Token(TokenType.PLUS, "+", 1, 4),
                new Token(TokenType.NUMBER, "5", 1, 6),
                new Token(TokenType.NUMBER, "10", 2, 1), // 10 - 5
                new Token(TokenType.MINUS, "-", 2, 4),
                new Token(TokenType.NUMBER, "5", 2, 6),
                new Token(TokenType.NUMBER, "10", 3, 1), // 10 * 5
                new Token(TokenType.ASTERISK, "*", 3, 4),
                new Token(TokenType.NUMBER, "5", 3, 6),
                new Token(TokenType.NUMBER, "10", 4, 1), // 10 / 5
                new Token(TokenType.SLASH, "/", 4, 4),
                new Token(TokenType.NUMBER, "5", 4, 6),
                new Token(TokenType.NUMBER, "10", 5, 1), // 10 ^ 5
                new Token(TokenType.CARET, "^", 5, 4),
                new Token(TokenType.NUMBER, "5", 5, 6),
                new Token(TokenType.IDENT, "ten", 6, 1), // ten = 10
                new Token(TokenType.ASSIGN, "=", 6, 5),
                new Token(TokenType.NUMBER, "10", 6, 7),
                new Token(TokenType.IDENT, "fifteen", 7, 1), // fifteen = ten + 5
                new Token(TokenType.ASSIGN, "=", 7, 9),
                new Token(TokenType.IDENT, "ten", 7, 11),
                new Token(TokenType.PLUS, "+", 7, 15),
                new Token(TokenType.NUMBER, "5", 7, 17),
                new Token(TokenType.NUMBER, "5", 8, 1), // 5 < x < 10
                new Token(TokenType.LT, "<", 8, 3),
                new Token(TokenType.IDENT, "x", 8, 5),
                new Token(TokenType.LT, "<", 8, 7),
                new Token(TokenType.NUMBER, "10", 8, 9),
                new Token(TokenType.IDENT, "x", 9, 1), // x <= 10
                new Token(TokenType.LTE, "<=", 9, 3),
                new Token(TokenType.NUMBER, "10", 9, 6),
                new Token(TokenType.IDENT, "x", 10, 1), // x > y
                new Token(TokenType.GT, ">", 10, 3),
                new Token(TokenType.IDENT, "y", 10, 5),
                new Token(TokenType.IDENT, "x", 11, 1), // x >= y
                new Token(TokenType.GTE, ">=", 11, 3),
                new Token(TokenType.IDENT, "y", 11, 6),
                new Token(TokenType.IDENT, "x", 12, 1), // x == y
                new Token(TokenType.EQ, "==", 12, 3),
                new Token(TokenType.IDENT, "y", 12, 6),
                new Token(TokenType.IDENT, "x", 13, 1), // x /= y
                new Token(TokenType.NOT_EQ, "/=", 13, 3),
                new Token(TokenType.IDENT, "y", 13, 6),
                new Token(TokenType.STRING, "Hello!", 14, 1), // "Hello!"
                new Token(TokenType.STRING, "Hello\nWorld!", 15, 1), // "Hello\nWorld!"
                new Token(TokenType.STRING, "Hello World!", 17, 1), // "Hello World!"
                new Token(TokenType.IDENT, "f", 18, 1), // f(x) = x + 10
                new Token(TokenType.LPAREN, "(", 18, 2),
                new Token(TokenType.IDENT, "x", 18, 3),
                new Token(TokenType.RPAREN, ")", 18, 4),
                new Token(TokenType.ASSIGN, "=", 18, 6),
                new Token(TokenType.IDENT, "x", 18, 8),
                new Token(TokenType.PLUS, "+", 18, 10),
                new Token(TokenType.NUMBER, "10", 18, 12),
                new Token(TokenType.IDENT, "abs", 19, 1), // abs(x) = case x < 0 -> -x, x >= 0 -> x
                new Token(TokenType.LPAREN, "(", 19, 4),
                new Token(TokenType.IDENT, "x", 19, 5),
                new Token(TokenType.RPAREN, ")", 19, 6),
                new Token(TokenType.ASSIGN, "=", 19, 8),
                new Token(TokenType.CASE, "case", 19, 10),
                new Token(TokenType.IDENT, "x", 19, 15),
                new Token(TokenType.LT, "<", 19, 17),
                new Token(TokenType.NUMBER, "0", 19, 19),
                new Token(TokenType.ARROW, "->", 19, 21),
                new Token(TokenType.MINUS, "-", 19, 24),
                new Token(TokenType.IDENT, "x", 19, 25),
                new Token(TokenType.COMMA, ",", 19, 26),
                new Token(TokenType.IDENT, "x", 19, 28),
                new Token(TokenType.GTE, ">=", 19, 30),
                new Token(TokenType.NUMBER, "0", 19, 33),
                new Token(TokenType.ARROW, "->", 19, 35),
                new Token(TokenType.IDENT, "x", 19, 38),
                new Token(TokenType.IDENT, "fib", 20, 1), // fib(x) =
                new Token(TokenType.LPAREN, "(", 20, 4),
                new Token(TokenType.IDENT, "x", 20, 5),
                new Token(TokenType.RPAREN, ")", 20, 6),
                new Token(TokenType.ASSIGN, "=", 20, 8),
                new Token(TokenType.CASE, "case", 21, 5), // ....case
                new Token(TokenType.IDENT, "x", 22, 5), // ....x == 0 -> 0,
                new Token(TokenType.EQ, "==", 22, 7),
                new Token(TokenType.NUMBER, "0", 22, 10),
                new Token(TokenType.ARROW, "->", 22, 12),
                new Token(TokenType.NUMBER, "0", 22, 15),
                new Token(TokenType.COMMA, ",", 22, 16),
                new Token(TokenType.IDENT, "x", 23, 5), // ....x == 1 -> 1,
                new Token(TokenType.EQ, "==", 23, 7),
                new Token(TokenType.NUMBER, "1", 23, 10),
                new Token(TokenType.ARROW, "->", 23, 12),
                new Token(TokenType.NUMBER, "1", 23, 15),
                new Token(TokenType.COMMA, ",", 23, 16),
                new Token(TokenType.DEFAULT, "default", 24, 5), // ....default -> fib(x - 2) + fib(x - 1)
                new Token(TokenType.ARROW, "->", 24, 13),
                new Token(TokenType.IDENT, "fib", 24, 16),
                new Token(TokenType.LPAREN, "(", 24, 19),
                new Token(TokenType.IDENT, "x", 24, 20),
                new Token(TokenType.MINUS, "-", 24, 22),
                new Token(TokenType.NUMBER, "2", 24, 24),
                new Token(TokenType.RPAREN, ")", 24, 25),
                new Token(TokenType.PLUS, "+", 24, 27),
                new Token(TokenType.IDENT, "fib", 24, 29),
                new Token(TokenType.LPAREN, "(", 24, 32),
                new Token(TokenType.IDENT, "x", 24, 33),
                new Token(TokenType.MINUS, "-", 24, 35),
                new Token(TokenType.NUMBER, "1", 24, 37),
                new Token(TokenType.RPAREN, ")", 24, 38),
                new Token(TokenType.EOF, "", 25, 1) // EOF
        );

        Lexer lexer = new Lexer(input);
        for (int i = 0; i < expectedList.size(); i++) {
            Token expected = expectedList.get(i);
            Token token = lexer.nextToken();

            assertThat(token)
                    .overridingErrorMessage("Test %d failed\nexpected: %s\n but was: %s", i, expected, token)
                    .isEqualTo(expected);
        }
    }

}