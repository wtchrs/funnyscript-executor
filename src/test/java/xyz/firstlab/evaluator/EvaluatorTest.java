package xyz.firstlab.evaluator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import xyz.firstlab.evaluator.object.BooleanValue;
import xyz.firstlab.evaluator.object.NumberValue;
import xyz.firstlab.evaluator.object.Value;
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
        testNumberValue(expected, value);
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
                    (1 < 2) == true | true
                    (1 < 2) == false | false
                    (1 > 2) == true | false
                    (1 > 2) == false | true
                    """
    )
    void testBooleanExpression(String input, boolean expected) {
        Value value = testEval(input);
        testBooleanValue(expected, value);
    }

    Value testEval(String input) {
        Lexer lexer = new Lexer(input);
        Parser parser = new DefaultParser(lexer);
        Program program = parser.parseProgram();
        return Evaluator.evaluate(program);
    }

    private static void testNumberValue(String expected, Value value) {
        assertThat(value)
                .withFailMessage("value type is wrong. expected: NumberValue, got: %s", value.getClass())
                .isInstanceOf(NumberValue.class);

        NumberValue numberValue = (NumberValue) value;
        assertThat(numberValue.getValue().compareTo(new BigDecimal(expected)))
                .withFailMessage("numberValue.value is wrong. expected: %s, got: %s", expected, numberValue.getValue())
                .isEqualTo(0);
    }

    private static void testBooleanValue(boolean expected, Value value) {
        assertThat(value)
                .withFailMessage("value type is wrong. expected: NumberValue, got: %s", value.getClass())
                .isInstanceOf(BooleanValue.class);

        BooleanValue booleanValue = (BooleanValue) value;
        assertThat(booleanValue.getValue())
                .withFailMessage(
                        "booleanValue.value is wrong. expected: %s, got: %s", expected, booleanValue.getValue())
                .isEqualTo(expected);
    }

}