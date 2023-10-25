package xyz.firstlab.parser.parselet;

import xyz.firstlab.parser.Parser;
import xyz.firstlab.parser.ParsingErrorException;
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
            throw new ParsingErrorException(
                    curToken, String.format("Could not parse `%s` as Number.", curToken.getLiteral())
            );
        }
    }

}
