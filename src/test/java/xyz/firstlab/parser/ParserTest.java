package xyz.firstlab.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import xyz.firstlab.parser.ast.*;
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
    void identifierParsing() {
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

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    +15 | + | 15
                    -15 | - | 15
                    """
    )
    void prefixExpressionParsing(String input, String operator, String value) {
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Program program = parser.parseProgram();
        checkParserErrors(parser);

        List<Expression> expressions = program.getExpressions();
        assertThat(expressions)
                .withFailMessage(
                        "program.expressions has the wrong number of elements.\n expected: 1, got: %d",
                        expressions.size())
                .hasSize(1);

        testPrefixExpression(expressions.get(0), operator, new BigDecimal(value));
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

    private static void testNumberLiteral(Expression exp, String expected) {
        testNumberLiteral(exp, new BigDecimal(expected));
    }

    private static void testNumberLiteral(Expression exp, BigDecimal expected) {
        assertThat(exp)
                .withFailMessage("exp type is wrong.\n expected: NumberLiteral, got: %s", exp.getClass())
                .isInstanceOf(NumberLiteral.class);

        BigDecimal value = ((NumberLiteral) exp).getValue();
        assertThat(value)
                .withFailMessage("literal.value is wrong.\n expected: %s, got: %s", expected, value)
                .isEqualTo(expected);
    }

    private void testIdentifier(Expression exp, String expected) {
        assertThat(exp)
                .withFailMessage("exp type is wrong.\n expected: Identifier, got: %s", exp.getClass())
                .isInstanceOf(Identifier.class);

        String value = ((Identifier) exp).getValue();
        assertThat(value)
                .withFailMessage("identifier.value is wrong.\n expected: %s, got: %s", expected, value)
                .isEqualTo(expected);
    }

    private void testPrefixExpression(Expression exp, String operator, Object right) {
        assertThat(exp)
                .withFailMessage("exp type is wrong.\n expected: PrefixExpression, got: %s", exp.getClass())
                .isInstanceOf(PrefixExpression.class);

        PrefixExpression prefix = (PrefixExpression) exp;
        assertThat(prefix.getOperator())
                .withFailMessage(
                        "prefix.operator is wrong.\n expected: %s, got: %s",
                        operator,
                        prefix.getOperator())
                .isEqualTo(operator);

        if (right instanceof BigDecimal expected) {
            testNumberLiteral(prefix.getRight(), expected);
        } else {
            fail("type of right is not handled.\n got: %s", right.getClass());
        }
    }

}