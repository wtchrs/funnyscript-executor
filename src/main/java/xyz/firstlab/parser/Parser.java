package xyz.firstlab.parser;

import xyz.firstlab.parser.ast.Expression;
import xyz.firstlab.parser.ast.NumberLiteral;
import xyz.firstlab.parser.ast.Program;
import xyz.firstlab.token.Lexer;
import xyz.firstlab.token.Token;
import xyz.firstlab.token.TokenType;

import java.util.*;

public class Parser {

    private final Lexer lexer;

    private final List<ParsingError> errors = new ArrayList<>();

    private Token curToken;

    private Token peekToken;

    private final Map<TokenType, PrefixParseFn> prefixParseFnMap = Map.of(
            TokenType.NUMBER, this::parseNumber
    );

    private final Map<TokenType, InfixParseFn> infixParseFnMap = Map.of();

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        nextToken();
        nextToken();
    }

    public Program parseProgram() {
        Program program = new Program();

        while (!curTokenIs(TokenType.EOF)) {
            Expression expr = parseExpression(Precedence.LOWEST);

            if (expr != null) {
                program.append(expr);
            }

            nextToken();
        }

        return program;
    }

    public List<ParsingError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    private void nextToken() {
        curToken = peekToken;
        peekToken = lexer.nextToken();
    }

    private boolean curTokenIs(TokenType type) {
        return curToken.getType().equals(type);
    }

    private boolean peekTokenIs(TokenType type) {
        return peekToken.getType().equals(type);
    }

    private Precedence curPrecendence() {
        return Precedence.getPrecedence(curToken.getType());
    }

    private Precedence peekPrecedence() {
        return Precedence.getPrecedence(peekToken.getType());
    }

    private Expression parseExpression(Precedence precedence) {
        PrefixParseFn prefix = prefixParseFnMap.get(curToken.getType());
        if (prefix == null) {
            noPrefixParseFnError(curToken);
            return null;
        }
        Expression leftExpr = prefix.parse();

        while (!peekTokenIs(TokenType.NEWLINE) && precedence.getValue() < peekPrecedence().getValue()) {
            InfixParseFn infix = infixParseFnMap.get(peekToken.getType());
            if (infix == null) {
                return leftExpr;
            }
            leftExpr = infix.parse(leftExpr);
        }

        return leftExpr;
    }

    private Expression parseNumber() {
        try {
            return new NumberLiteral(curToken, curToken.getLiteral());
        } catch (NumberFormatException e) {
            wrongNumberFormatError(curToken);
            return null;
        }
    }

    private void noPrefixParseFnError(Token token) {
        errors.add(new ParsingError(
                token.getLineNumber(),
                token.getColumnNumber(),
                String.format("No prefix parse function for %s found.", token.getType())
        ));
    }

    private void wrongNumberFormatError(Token token) {
        errors.add(new ParsingError(
                token.getLineNumber(),
                token.getColumnNumber(),
                String.format("Could not parse `%s` as Number.", token.getLiteral())
        ));
    }

}
