package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.ParsingError;
import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.NumberLiteral;
import xyz.firstlab.token.Token;

public class NumberParselet implements PrefixParselet {

    @Override
    public Expression parse(Parser parser) {
        Token curToken = parser.getCurToken();
        try {
            return new NumberLiteral(curToken, curToken.getLiteral());
        } catch (NumberFormatException e) {
            parser.appendError(wrongNumberFormatError(curToken));
            return null;
        }
    }

    private ParsingError wrongNumberFormatError(Token token) {
        return new ParsingError(
                token.getLineNumber(),
                token.getColumnNumber(),
                String.format("Could not parse `%s` as Number.", token.getLiteral())
        );
    }

}
