package xyz.firstlab.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import xyz.firstlab.ast.*;
import xyz.firstlab.ast.ast.*;
import xyz.firstlab.parser.ast.*;
import xyz.firstlab.lexer.Lexer;

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
    void booleanLiteralParsing(String input, boolean expected) {
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

        testBooleanLiteral(expressions.get(0), expected);
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    "hello" | hello
                    "hello, world!" | hello, world!
                    """
    )
    void stringLiteralParsing(String input, String expected) {
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

        testStringLiteral(expressions.get(0), expected);
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
                    x = 10 | x | 10
                    add(x, y) = x + y | add(x, y) | (x + y)
                    """
    )
    void assignExpressionParsing(String input, String leftExpected, String rightExpected) {
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

        Expression exp = expressions.get(0);
        assertThat(exp)
                .withFailMessage(
                        "exp type is wrong.\n expected: AssignExpression or FunctionAssignExpression, got: %s",
                        exp.getClass())
                .isInstanceOfAny(AssignExpression.class, FunctionAssignExpression.class);

        assertThat(exp.string()).isEqualTo(String.format("(%s = %s)", leftExpected, rightExpected));
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    case x < 0 -> 5, x >= 0 -> 10 | (case ((x < 0) -> 5), ((x >= 0) -> 10))
                    abs(x) = case x < 0 -> -x, default -> x | (abs(x) = (case ((x < 0) -> (-x)), (true -> x)))
                    """
    )
    void caseExpressionParsing(String input, String expected) {
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
                    true and true | and | true | true
                    true and false | and | true | false
                    true or false | or | true | false
                    false or false | or | false | false
                    """
    )
    void booleanInfixExpressionParsing(String input, String operator, Boolean left, Boolean right) {
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

        testInfixExpression(expressions.get(0), operator, left, right);
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
                    3 < x and x < 5 | ((3 < x) and (x < 5))
                    x < 3 or x > 5 | ((x < 3) or (x > 5))
                    x < y + 3 or x > y + 5 | ((x < (y + 3)) or (x > (y + 5)))
                    1 + (2 + 3) + 4 | ((1 + (2 + 3)) + 4)
                    (5 + 5) * 2 | ((5 + 5) * 2)
                    2 / ( 5 + 5) | (2 / (5 + 5))
                    -(5 + 5) | (-(5 + 5))
                    not (true == true) | (not (true == true))
                    x = y = 10 | (x = (y = 10))
                    x = y = 5 + 10 * 2 | (x = (y = (5 + (10 * 2))))
                    a + add(b * c) + d | ((a + add((b * c))) + d)
                    add(a, b, 1, 2 * 3, 4 + 5, add(6, 7 * 8)) | add(a, b, 1, (2 * 3), (4 + 5), add(6, (7 * 8)))
                    add(a + b + c * d / f + g) | add((((a + b) + ((c * d) / f)) + g))
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
                        String.join("\n", errorStrings)))
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

    private static void testStringLiteral(Expression exp, String expected) {
        assertThat(exp)
                .withFailMessage("exp type is wrong.\n expected: StringLiteral, got: %s", exp.getClass())
                .isInstanceOf(StringLiteral.class);

        String value = ((StringLiteral) exp).getValue();
        assertThat(value)
                .withFailMessage("literal.value is wrong.\n expected: %s, got: %s", expected, value)
                .isEqualTo(expected);
    }

    private static void testIdentifier(Expression exp, String expected) {
        assertThat(exp)
                .withFailMessage("exp type is wrong.\n expected: Identifier, got: %s", exp.getClass())
                .isInstanceOf(Identifier.class);

        String value = ((Identifier) exp).getValue();
        assertThat(value)
                .withFailMessage("identifier.value is wrong.\n expected: %s, got: %s", expected, value)
                .isEqualTo(expected);
    }

    private static void testPrefixExpression(Expression exp, String operator, Object right) {
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

    private static void testInfixExpression(Expression exp, String operator, Object left, Object right) {
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
        } else if (expectedValue instanceof String expected) {
            testIdentifier(value, expected);
        } else {
            fail("type of expectedValue is not handled.\n got: %s", expectedValue.getClass());
        }
    }

}