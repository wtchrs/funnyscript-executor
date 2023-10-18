package xyz.firstlab.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import xyz.firstlab.parser.ast.*;
import xyz.firstlab.token.Lexer;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ParserTest {

    @ParameterizedTest
    @CsvSource({"5", "10", "0", "0.1", "1.", "1.0", ".1"})
    void numberLiteralParsing(String input) {
        Lexer lexer = new Lexer(input);
        Parser parser = new DefaultParser(lexer);
        Program program = parser.parseProgram();
        checkParserErrors(parser);

        List<Expression> expressions = program.getExpressions();
        assertThat(expressions)
                .withFailMessage(
                        "program.expressions has the wrong number of elements.\n expected: 1, got: %d",
                        expressions.size())
                .hasSize(1);

        testNumberLiteral(expressions.get(0), input);
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    true | true
                    false | false
                    """
    )
    void booleanLiteralParsing(String input, boolean value) {
        Lexer lexer = new Lexer(input);
        Parser parser = new DefaultParser(lexer);
        Program program = parser.parseProgram();
        checkParserErrors(parser);

        List<Expression> expressions = program.getExpressions();
        assertThat(expressions)
                .withFailMessage(
                        "program.expressions has the wrong number of elements.\n expected: 1, got: %d",
                        expressions.size())
                .hasSize(1);

        testBooleanLiteral(expressions.get(0), value);
    }

    @ParameterizedTest
    @CsvSource({"foo", "bar"})
    void identifierParsing(String input) {
        Lexer lexer = new Lexer(input);
        Parser parser = new DefaultParser(lexer);
        Program program = parser.parseProgram();
        checkParserErrors(parser);

        List<Expression> expressions = program.getExpressions();
        assertThat(expressions)
                .withFailMessage(
                        "program.expressions has the wrong number of elements.\n expected: 1, got: %d",
                        expressions.size())
                .hasSize(1);

        testIdentifier(expressions.get(0), input);
    }

    static Stream<Arguments> providePrefixExpressionParsingArguments() {
        return Stream.of(
                Arguments.of("+15", "+", new BigDecimal("15")),
                Arguments.of("-15", "-", new BigDecimal("15")),
                Arguments.of("not true", "not", true)
        );
    }

    @ParameterizedTest
    @MethodSource("providePrefixExpressionParsingArguments")
    void prefixExpressionParsing(String input, String operator, Object value) {
        Lexer lexer = new Lexer(input);
        Parser parser = new DefaultParser(lexer);
        Program program = parser.parseProgram();
        checkParserErrors(parser);

        List<Expression> expressions = program.getExpressions();
        assertThat(expressions)
                .withFailMessage(
                        "program.expressions has the wrong number of elements.\n expected: 1, got: %d",
                        expressions.size())
                .hasSize(1);

        testPrefixExpression(expressions.get(0), operator, value);
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    5 + 10 | + | 5 | 10
                    5 - 10 | - | 5 | 10
                    5 * 10 | * | 5 | 10
                    5 / 10 | / | 5 | 10
                    5 ^ 10 | ^ | 5 | 10
                    5 < 10 | < | 5 | 10
                    5 > 10 | > | 5 | 10
                    5 <= 10 | <= | 5 | 10
                    5 >= 10 | >= | 5 | 10
                    """
    )
    void infixExpressionParsing(String input, String operator, String left, String right) {
        Lexer lexer = new Lexer(input);
        Parser parser = new DefaultParser(lexer);
        Program program = parser.parseProgram();
        checkParserErrors(parser);

        List<Expression> expressions = program.getExpressions();
        assertThat(expressions)
                .withFailMessage(
                        "program.expressions has the wrong number of elements.\n expected: 1, got: %d",
                        expressions.size())
                .hasSize(1);

        testInfixExpression(expressions.get(0), operator, new BigDecimal(left), new BigDecimal(right));
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    -a * b | ((-a) * b)
                    a + b + c | ((a + b) + c)
                    a + b - c | ((a + b) - c)
                    a * b * c | ((a * b) * c)
                    a * b / c | ((a * b) / c)
                    a + b / c | (a + (b / c))
                    a + b * c + d / e - f | (((a + (b * c)) + (d / e)) - f)
                    5 > 4 == 3 < 4 | ((5 > 4) == (3 < 4))
                    5 < 4 /= 3 > 4 | ((5 < 4) /= (3 > 4))
                    3 + 4 * 5 == 3 * 1 + 4 * 5 | ((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))
                    3 > 5 == false | ((3 > 5) == false)
                    3 < 5 == true | ((3 < 5) == true)
                    true == 3 < 5 | (true == (3 < 5))
                    1 + (2 + 3) + 4 | ((1 + (2 + 3)) + 4)
                    (5 + 5) * 2 | ((5 + 5) * 2)
                    2 / ( 5 + 5) | (2 / (5 + 5))
                    -(5 + 5) | (-(5 + 5))
                    not (true == true) | (not (true == true))
                    """
    )
    void operatorPrecedenceParsing(String input, String expected) {
        Lexer lexer = new Lexer(input);
        Parser parser = new DefaultParser(lexer);
        Program program = parser.parseProgram();
        checkParserErrors(parser);

        List<Expression> expressions = program.getExpressions();
        assertThat(expressions)
                .withFailMessage(
                        "program.expressions has the wrong number of elements.\n expected: 1, got: %d",
                        expressions.size())
                .hasSize(1);

        String result = expressions.get(0).string();
        assertThat(result)
                .withFailMessage("Parsing result is wrong. expected: %s, got: %s", expected, result)
                .isEqualTo(expected);
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

    private static void testBooleanLiteral(Expression exp, boolean expected) {
        assertThat(exp)
                .withFailMessage("exp type is wrong.\n expected: BooleanLiteral, got: %s", exp.getClass())
                .isInstanceOf(BooleanLiteral.class);

        boolean value = ((BooleanLiteral) exp).getValue();
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

        testOperand(prefix.getRight(), right);
    }

    private void testInfixExpression(Expression exp, String operator, Object left, Object right) {
        assert left.getClass() == right.getClass();

        assertThat(exp)
                .withFailMessage("exp type is wrong.\n expected: InfixExpression, got: %s", exp.getClass())
                .isInstanceOf(InfixExpression.class);

        InfixExpression infix = (InfixExpression) exp;
        assertThat(infix.getOperator())
                .withFailMessage(
                        "infix.operator is wrong.\n expected: %s, got: %s",
                        operator,
                        infix.getOperator())
                .isEqualTo(operator);

        testOperand(infix.getLeft(), left);
        testOperand(infix.getRight(), right);
    }

    private static void testOperand(Expression value, Object expectedValue) {
        if (expectedValue instanceof BigDecimal expected) {
            testNumberLiteral(value, expected);
        } else if (expectedValue instanceof Boolean expected) {
            testBooleanLiteral(value, expected);
        } else {
            fail("type of expectedValue is not handled.\n got: %s", expectedValue.getClass());
        }
    }

}