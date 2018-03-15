package com.jd.auction.common.core.parsing.lexer.analyzer;



import com.jd.auction.common.core.parsing.lexer.token.DefaultKeyword;
import com.jd.auction.common.core.parsing.lexer.token.Keyword;
import com.jd.auction.common.core.parsing.lexer.token.TokenType;

import java.util.HashMap;
import java.util.Map;



public final class Dictionary {
    
    private final Map<String, Keyword> tokens = new HashMap<>(1024);
    
    public Dictionary(final Keyword... dialectKeywords) {
        fill(dialectKeywords);
    }
    
    private void fill(final Keyword... dialectKeywords) {
        for (DefaultKeyword each : DefaultKeyword.values()) {
            tokens.put(each.name(), each);
        }
        for (Keyword each : dialectKeywords) {
            tokens.put(each.toString(), each);
        }
    }
    
    TokenType findTokenType(final String literals, final TokenType defaultTokenType) {
        String key = null == literals ? null : literals.toUpperCase();
        return tokens.containsKey(key) ? tokens.get(key) : defaultTokenType;
    }
    
    TokenType findTokenType(final String literals) {
        String key = null == literals ? null : literals.toUpperCase();
        if (tokens.containsKey(key)) {
            return tokens.get(key);
        }
        throw new IllegalArgumentException();
    }
}
