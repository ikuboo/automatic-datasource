package com.jd.auction.common.automatic.parsing.lexer.token;



public final class Token {
    
    private final TokenType type;
    
    private final String literals;
    
    private final int endPosition;

    public Token(TokenType type, String literals, int endPosition) {
        this.type = type;
        this.literals = literals;
        this.endPosition = endPosition;
    }

    public TokenType getType() {
        return type;
    }

    public String getLiterals() {
        return literals;
    }

    public int getEndPosition() {
        return endPosition;
    }
}
