package com.jd.auction.common.automatic.parsing.lexer.dialect.postgresql;


import com.jd.auction.common.automatic.parsing.lexer.Lexer;
import com.jd.auction.common.automatic.parsing.lexer.analyzer.Dictionary;


public final class PostgreSQLLexer extends Lexer {
    
    private static Dictionary dictionary = new Dictionary(PostgreSQLKeyword.values());
    
    public PostgreSQLLexer(final String input) {
        super(input, dictionary);
    }
}
