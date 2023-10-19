package xyz.firstlab.parser;

import xyz.firstlab.parser.ast.*;
import xyz.firstlab.parser.parselet.InfixParselet;
import xyz.firstlab.parser.parselet.PrefixParselet;
import xyz.firstlab.token.Lexer;
import xyz.firstlab.token.Token;
import xyz.firstlab.token.TokenType;

import java.util.*;

public abstract class Parser {

    private final Lexer lexer;

    private final List<ParsingError> errors = new ArrayList<>();

    private Token curToken;

    private Token peekToken;

    private final Map<TokenType, PrefixParselet> prefixParseletMap = new HashMap<>();

    private final Map<TokenType, InfixParselet> infixParseletMap = new HashMap<>();

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        nextToken();
        nextToken();
    }

    public Program parseProgram() {
        Program program = new Program();

        while (!curTokenIs(TokenType.EOF)) {
            Expression exp = parseExpression(Precedence.LOWEST.getValue());

            if (exp != null) {
                program.append(exp);
            }

            nextToken();
        }

        return program;
    }

    // Pratt parser
    public Expression parseExpression(int precedence) {
        PrefixParselet prefix = prefixParseletMap.get(curToken.getType());
        if (prefix == null) {
            appendError(noPrefixParseletError(curToken));
            return null;
        }
        Expression leftExp = prefix.parse(this);

        while (!peekTokenIs(TokenType.NEWLINE) && precedence < peekPrecedence().getValue()) {
            InfixParselet infix = infixParseletMap.get(peekToken.getType());
            if (infix == null) {
                return leftExp;
            }
            nextToken();
            leftExp = infix.parse(this, leftExp);
        }

        return leftExp;
    }

    public List<ParsingError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public void appendError(ParsingError error) {
        errors.add(error);
    }

    public Token getCurToken() {
        return curToken;
    }

    public Token getPeekToken() {
        return peekToken;
    }

    public boolean curTokenIs(TokenType type) {
        return curToken.getType().equals(type);
    }

    public boolean peekTokenIs(TokenType type) {
        return peekToken.getType().equals(type);
    }

    public boolean expectPeek(TokenType tokenType) {
        if (peekTokenIs(tokenType)) {
            nextToken();
            return true;
        }
        appendError(notExpectedTokenError(tokenType, peekToken));
        return false;
    }

    public void nextToken() {
        curToken = peekToken;
        peekToken = lexer.nextToken();
    }

    protected void register(TokenType type, PrefixParselet parselet) {
        this.prefixParseletMap.put(type, parselet);
    }

    protected void register(TokenType type, InfixParselet parselet) {
        this.infixParseletMap.put(type, parselet);
    }

    private Precedence curPrecendence() {
        if (infixParseletMap.containsKey(curToken.getType())) {
            return infixParseletMap.get(curToken.getType()).getPrecedence();
        }
        return Precedence.LOWEST;
    }

    private Precedence peekPrecedence() {
        if (infixParseletMap.containsKey(peekToken.getType())) {
            return infixParseletMap.get(peekToken.getType()).getPrecedence();
        }
        return Precedence.LOWEST;
    }

    private ParsingError noPrefixParseletError(Token token) {
        return new ParsingError(
                token.getLineNumber(),
                token.getColumnNumber(),
                String.format("No prefix parse function for '%s' found.", token.getType().getValue())
        );
    }

    private ParsingError notExpectedTokenError(TokenType expected, Token token) {
        return new ParsingError(
                token.getLineNumber(),
                token.getColumnNumber(),
                String.format("Expected '%s', but got '%s' instead.", expected.getValue(), token.getType().getValue())
        );
    }

}
