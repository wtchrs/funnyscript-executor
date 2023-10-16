package xyz.firstlab.parser;

import org.junit.jupiter.api.Test;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.Identifier;
import xyz.firstlab.parser.ast.NumberLiteral;
import xyz.firstlab.parser.ast.Program;
import xyz.firstlab.token.Lexer;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ParserTest {

    @Test
    void numberLiteralParsing() {
        List<String> tests = List.of("5", "10", "0", "0.1", "1.", "1.0", ".1");

        for (String test : tests) {
            Lexer lexer = new Lexer(test);
            Parser parser = new Parser(lexer);
            Program program = parser.parseProgram();
            checkParserErrors(parser);

            List<Expression> expressions = program.getExpressions();
            assertThat(expressions)
                    .withFailMessage(
                            "program.expressions has the wrong number of elements.\n expected: 1, got: %d",
                            expressions.size())
                    .hasSize(1);

            testNumberLiteral(expressions.get(0), test);
        }
    }

    @Test
    void IdentifierParsing() {
        List<String> tests = List.of("foo", "bar");

        for (String test : tests) {
            Lexer lexer = new Lexer(test);
            Parser parser = new Parser(lexer);
            Program program = parser.parseProgram();
            checkParserErrors(parser);

            List<Expression> expressions = program.getExpressions();
            assertThat(expressions)
                    .withFailMessage(
                            "program.expressions has the wrong number of elements.\n expected: 1, got: %d",
                            expressions.size())
                    .hasSize(1);

            testIdentifier(expressions.get(0), test);
        }
    }

    void checkParserErrors(Parser parser) {
        List<String> errorStrings = parser.getErrors().stream().map(ParsingError::toString).toList();

        assertThat(errorStrings)
                .withFailMessage(() -> String.format(
                        "parser has %d errors:\n%s",
                        errorStrings.size(),
                        String.join("\n    ", errorStrings)))
                .hasSize(0);
    }

    private static void testNumberLiteral(Expression expr, String expected) {
        assertThat(expr)
                .withFailMessage("exp type is wrong.\n expected: NumberLiteral, got: %s", expr.getClass())
                .isInstanceOf(NumberLiteral.class);

        BigDecimal value = ((NumberLiteral) expr).getValue();
        assertThat(value)
                .withFailMessage("literal.value is wrong.\n expected: %s, got: %s", expected, value)
                .isEqualTo(new BigDecimal(expected));
    }

    private void testIdentifier(Expression expression, String expected) {
        assertThat(expression)
                .withFailMessage("exp type is wrong.\n expected: Identifier, got: %s", expression.getClass())
                .isInstanceOf(Identifier.class);

        String value = ((Identifier) expression).getValue();
        assertThat(value)
                .withFailMessage("identifier.value is wrong.\n expected: %s, got: %s", expected, value)
                .isEqualTo(expected);
    }

}