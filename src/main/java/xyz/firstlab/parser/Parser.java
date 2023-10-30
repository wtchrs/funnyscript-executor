package xyz.firstlab.parser;

import xyz.firstlab.ast.*;
import xyz.firstlab.parser.parselet.InfixParselet;
import xyz.firstlab.parser.parselet.PrefixParselet;
import xyz.firstlab.lexer.Lexer;
import xyz.firstlab.lexer.Token;
import xyz.firstlab.lexer.TokenType;

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
            Expression exp = null;

            try {
                exp = parseExpression(Precedence.LOWEST);
            } catch (ParsingErrorException e) {
                ParsingError error = new ParsingError(e.getLineNumber(), e.getColumnNumber(), e.getMessage());
                errors.add(error);
            }

            if (exp != null) {
                program.append(exp);
            }

            nextToken();
        }

        return program;
    }

    public Expression parseExpression(Precedence precedence) {
        return parseExpression(precedence.getValue());
    }

    // Pratt parser
    public Expression parseExpression(int precedence) {
        PrefixParselet prefix = prefixParseletMap.get(curToken.getType());
        if (prefix == null) {
            Token curToken = getCurToken();
            nextToken();
            String message =
                    String.format("No prefix or infix parselet for '%s' found.", curToken.getType().getValue());
            throw new ParsingErrorException(curToken, message);
        }
        Expression leftExp = prefix.parse(this);

        while (precedence < peekPrecedence().getValue()) {
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

    public void assertPeekIs(TokenType tokenType) {
        if (peekTokenIs(tokenType)) {
            nextToken();
            return;
        }

        String message = String.format(
                "Expected '%s', but got '%s' instead.",
                tokenType.getValue(), peekToken.getType().getValue()
        );
        throw new ParsingErrorException(peekToken, message);
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

}
