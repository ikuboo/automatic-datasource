package com.jd.auction.common.core.parsing.lexer.dialect.postgresql;


import com.jd.auction.common.core.parsing.lexer.Lexer;
import com.jd.auction.common.core.parsing.lexer.analyzer.Dictionary;


public final class PostgreSQLLexer extends Lexer {
    
    private static Dictionary dictionary = new Dictionary(PostgreSQLKeyword.values());
    
    public PostgreSQLLexer(final String input) {
        super(input, dictionary);
    }
}
