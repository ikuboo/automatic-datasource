package com.jd.auction.common.automatic.parsing.lexer.dialect.sqlserver;


import com.jd.auction.common.automatic.parsing.lexer.Lexer;
import com.jd.auction.common.automatic.parsing.lexer.analyzer.Dictionary;


public final class SQLServerLexer extends Lexer {
    
    private static Dictionary dictionary = new Dictionary(SQLServerKeyword.values());
    
    public SQLServerLexer(final String input) {
        super(input, dictionary);
    }
    
    @Override
    protected boolean isHintBegin() {
        return '/' == getCurrentChar(0) && '*' == getCurrentChar(1) && '!' == getCurrentChar(2);
    }
    
    @Override
    protected boolean isVariableBegin() {
        return '@' == getCurrentChar(0);
    }
    
    @Override
    protected boolean isSupportNChars() {
        return true;
    }
}
