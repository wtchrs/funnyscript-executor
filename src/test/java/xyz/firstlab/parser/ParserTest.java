package xyz.firstlab.parser;

import org.junit.jupiter.api.Test;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.NumberLiteral;
import xyz.firstlab.parser.ast.Program;
import xyz.firstlab.token.Lexer;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ParserTest {

    @Test
    void numberLiteralParsing() {
        String input = "5";

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

        testNumberLiteral(expressions.get(0), "5");
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
                .withFailMessage("expr type is wrong.\n expected: NumberLiteral, got: %s", expr.getClass().toString())
                .isInstanceOf(NumberLiteral.class);

        BigDecimal value = ((NumberLiteral) expr).getValue();
        assertThat(value)
                .withFailMessage(
                        "literal.value is wrong.\n expected: %s, got: %s", expected, value)
                .isEqualTo(new BigDecimal(expected));
    }

}