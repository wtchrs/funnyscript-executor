package xyz.firstlab.evaluator;

import ch.obermuhlner.math.big.BigDecimalMath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import xyz.firstlab.evaluator.object.*;
import xyz.firstlab.parser.DefaultParser;
import xyz.firstlab.parser.Parser;
import xyz.firstlab.ast.Program;
import xyz.firstlab.lexer.Lexer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

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

        testNumberValue(value, expected);
        testNumberValue(env.get(variableName), expected);
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    case 1 < 2 -> 10, default -> 5 | 10
                    case 1 > 2 -> 10, default -> 5 | 5
                    case false -> 10, true == false -> 5, default -> 1 | 1
                    """
    )
    void evalCaseExpression(String input, String expected) {
        Value value = testEval(input);
        testNumberValue(value, expected);
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
        testFunctionValue(env.get(functionName), parameters, body);
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    id(x) = x id(5) | 5
                    double(x) = 2 * x double(5) | 10
                    add(x, y) = x + y add(5, 5) | 10
                    add(x, y) = x + y add(5 + 5, add(5, 5)) | 20
                    (id(x) = x)(5) | 5
                    y = 10 x = 5 f(x) = y + x f(3) | 13
                    x = 10 f() = x f() | 10
                    """
    )
    void evalFunctionCallExpression(String input, String expected) {
        Value value = testEval(input);
        testNumberValue(value, expected);
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    sum(a, b) = a + b sum(5) | b | (a + b) | a | 5
                    f(a, b, c) = a * b + c f(2) | b, c | ((a * b) + c) | a | 2
                    f(a, b, c) = a * b + c f(2, 4) | c | ((a * b) + c) | a, b | 2, 4
                    """
    )
    void evalFunctionCurrying(
            String input, String params, String body, String capturedVarNames, String capturedValues) {
        Environment env = Environment.create();
        Value value = testEval(input, env);

        testFunctionValue(value, params, body);

        FunctionValue func = (FunctionValue) value;
        Environment funcEnv = func.getEnclosedEnv();
        String[] varNames = capturedVarNames.split(", ");
        String[] values = capturedValues.split(", ");

        testEnvVariables(varNames, funcEnv, values);
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            textBlock = """
                    abs(5) | 5
                    abs(-5) | 5
                    sqrt(4) | 2
                    min(5, 3, 4, 2) | 2
                    max(5, 3, 4, 2) | 5
                    log(100) | 2
                    log(2, 8) | 3
                    sin(PI / 2) | 1
                    cos(PI / 2) | 0
                    tan(PI) | 0
                    asin(sin(1)) | 1
                    acos(cos(1)) | 1
                    atan(tan(1)) | 1
                    toDegrees(PI / 2) | 90
                    toDegrees(toRadians(90)) | 90
                    """
    )
    void evalBuiltinFunction(String input, String expected) {
        Value value = testEval(input);
        testNumberValue(value, expected);
    }

    @Test
    void evalBuiltinAtan2() {
        String input = "atan2(1, 1)";
        Value value = testEval(input);
        MathContext context = new MathContext(100, RoundingMode.HALF_UP);
        BigDecimal expected = BigDecimalMath.pi(context)
                .divide(BigDecimal.valueOf(4), context)
                .setScale(50, RoundingMode.HALF_UP);
        testNumberValue(value, expected.toString());
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
                .withFailMessage("value type is wrong. expected: NUMBER, got: %s[%s]", value.type(), value.inspect())
                .isInstanceOf(NumberValue.class);

        NumberValue numberValue = (NumberValue) value;
        BigDecimal approximate = numberValue.getValue().setScale(50, RoundingMode.HALF_UP);
        assertThat(approximate.compareTo(new BigDecimal(expected)))
                .withFailMessage("numberValue.value is wrong. expected: %s, got: %s", expected, numberValue.getValue())
                .isEqualTo(0);
    }

    private static void testBooleanValue(Value value, boolean expected) {
        assertThat(value)
                .withFailMessage(
                        "value type is wrong. expected: BOOLEAN, got: %s[%s]", value.type(), value.inspect())
                .isInstanceOf(BooleanValue.class);

        BooleanValue booleanValue = (BooleanValue) value;
        assertThat(booleanValue.getValue())
                .withFailMessage(
                        "booleanValue.value is wrong. expected: %s, got: %s", expected, booleanValue.getValue())
                .isEqualTo(expected);
    }

    private static void testStringValue(Value value, String expected) {
        assertThat(value)
                .withFailMessage(
                        "value type is wrong. expected: STRING, got: %s[%s]", value.type(), value.inspect())
                .isInstanceOf(StringValue.class);

        StringValue stringValue = (StringValue) value;
        assertThat(stringValue.getValue())
                .withFailMessage("stringValue.value is wrong. expected: %s, got: %s", expected, stringValue.getValue())
                .isEqualTo(expected);
    }

    private void testFunctionValue(Value value, String parameters, String body) {
        assertThat(value)
                .withFailMessage("value type is wrong. expected: FUNCTION, got: %s[%s]", value.type(), value.inspect())
                .isInstanceOf(FunctionValue.class);

        FunctionValue functionValue = (FunctionValue) value;
        String expectedInspection = String.format("<FUNCTION(%s) = %s>", parameters, body);
        assertThat(functionValue.inspect())
                .withFailMessage(
                        "functionValue.value is wrong. expected: %s, got: %s",
                        expectedInspection, functionValue.inspect())
                .isEqualTo(expectedInspection);
    }

    private static void testEnvVariables(String[] varNames, Environment funcEnv, String[] values) {
        for (int i = 0; i < varNames.length; i++) {
            Value val = funcEnv.get(varNames[i]);
            assertThat(val.inspect())
                    .withFailMessage(
                            "[%d] Captured variable '%s' is wrong. expected: %s, got: %s",
                            i, varNames[i], values[i], val)
                    .isEqualTo(values[i]);
        }
    }

}