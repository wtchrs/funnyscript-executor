package xyz.firstlab.evaluator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import xyz.firstlab.evaluator.object.*;
import xyz.firstlab.parser.DefaultParser;
import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.ast.Program;
import xyz.firstlab.token.Lexer;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class EvaluatorTest {

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    5 | 5
                    5.5 | 5.5
                    -5 | -5
                    5 + 5 + 5 + 5 - 10 | 10
                    2 * 2 * 2 * 2 * 2 | 32
                    -50 + 100 + -50 | 0
                    5 * 2 + 10 | 20
                    5 + 2 * 10 | 25
                    20 + 2 * -10 | 0
                    50 / 2 * 2 + 10 | 60
                    2 * (5 + 10) | 30
                    3 * 3 * 3 + 10 | 37
                    3 * (3 * 3) + 10 | 37
                    (5 + 10 * 2 + 15 / 3) * 2 + -10 | 50
                    0.5 * 0.5 | 0.25
                    2 ^ 10 | 1024
                    2 ^ 2 ^ 2 ^ 2 | 65536
                    ((2 ^ 2) ^ 2) ^ 2 | 256
                    0.25 ^ 0.5 | 0.5
                    """
    )
    void evalNumberExpression(String input, String expected) {
        Value value = testEval(input);
        testNumberValue(value, expected);
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    true | true
                    false | false
                    1 < 2 | true
                    1 > 2 | false
                    1 < 1 | false
                    1 > 1 | false
                    1 <= 2 | true
                    1 >= 2 | false
                    1 <= 1 | true
                    1 >= 1 | true
                    1 == 1 | true
                    1 /= 1 | false
                    1 == 2 | false
                    1 /= 2 | true
                    true == true | true
                    false == false | true
                    true == false | false
                    true /= false | true
                    false /= true | true
                    1 < 2 == true | true
                    1 < 2 == false | false
                    1 > 2 == true | false
                    1 > 2 == false | true
                    1 < 2 and 5 /= 3 | true
                    1 < 2 and 5 < 3 | false
                    1 > 2 or 5 < 3 | false
                    1 > 2 or 5 /= 3 | true
                    """
    )
    void evalBooleanExpression(String input, boolean expected) {
        Value value = testEval(input);
        testBooleanValue(value, expected);
    }

    @Test
    void evalStringExpression() {
        String input = "\"hello, \" + \"world!\"";
        String expected = "hello, world!";
        Value value = testEval(input);
        testStringValue(value, expected);
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    x = 10 | x | 10
                    x = 10 + 5 | x | 15
                    """
    )
    void evalAssignExpression(String input, String variableName, String expected) {
        Environment env = Environment.create();
        Value value = testEval(input, env);

        assertThat(value.inspect())
                .withFailMessage("value is wrong. expected: %s, got: %s", expected, value.inspect())
                .isEqualTo(expected);

        String envValue = env.get(variableName).inspect();
        assertThat(envValue)
                .withFailMessage(
                        "Variable '%s' has wrong value. expected: %s, got: %s", variableName, expected, envValue)
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    f(x) = x + 10 | f | x | (x + 10)
                    add(a, b) = a + b | add | a, b | (a + b)
                    abs(x) = case x < 0 -> -x, default -> x | abs | x | (case ((x < 0) -> (-x)), (true -> x))
                    """
    )
    void evalFunctionAssignExpression(String input, String functionName, String parameters, String body) {
        Environment env = Environment.create();
        Value value = testEval(input, env);
        testFunctionValue(value, parameters, body);
    }

    Value testEval(String input) {
        Lexer lexer = new Lexer(input);
        Parser parser = new DefaultParser(lexer);
        Program program = parser.parseProgram();
        return Evaluator.evaluate(program);
    }

    Value testEval(String input, Environment env) {
        Lexer lexer = new Lexer(input);
        Parser parser = new DefaultParser(lexer);
        Program program = parser.parseProgram();
        return Evaluator.evaluate(program, env);
    }

    private static void testNumberValue(Value value, String expected) {
        assertThat(value)
                .withFailMessage("value type is wrong. expected: NumberValue, got: %s", value.getClass())
                .isInstanceOf(NumberValue.class);

        NumberValue numberValue = (NumberValue) value;
        assertThat(numberValue.getValue().compareTo(new BigDecimal(expected)))
                .withFailMessage("numberValue.value is wrong. expected: %s, got: %s", expected, numberValue.getValue())
                .isEqualTo(0);
    }

    private static void testBooleanValue(Value value, boolean expected) {
        assertThat(value)
                .withFailMessage("value type is wrong. expected: BooleanValue, got: %s", value.getClass())
                .isInstanceOf(BooleanValue.class);

        BooleanValue booleanValue = (BooleanValue) value;
        assertThat(booleanValue.getValue())
                .withFailMessage(
                        "booleanValue.value is wrong. expected: %s, got: %s", expected, booleanValue.getValue())
                .isEqualTo(expected);
    }

    private static void testStringValue(Value value, String expected) {
        assertThat(value)
                .withFailMessage("value type is wrong. expected: StringValue, got: %s", value.getClass())
                .isInstanceOf(StringValue.class);

        StringValue stringValue = (StringValue) value;
        assertThat(stringValue.getValue())
                .withFailMessage("stringValue.value is wrong. expected: %s, got: %s", expected, stringValue.getValue())
                .isEqualTo(expected);
    }

    private void testFunctionValue(Value value, String parameters, String body) {
        assertThat(value)
                .withFailMessage("value type is wrong. expected: FunctionValue, got: %s", value.getClass())
                .isInstanceOf(FunctionValue.class);

        FunctionValue functionValue = (FunctionValue) value;
        String expectedInspection = String.format("<FUNCTION(%s) = %s>", parameters, body);
        assertThat(functionValue.inspect())
                .withFailMessage(
                        "functionValue.value is wrong. expected: %s, got: %s",
                        expectedInspection, functionValue.inspect())
                .isEqualTo(expectedInspection);
    }

}