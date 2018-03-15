package com.jd.auction.common.core.parsing;


import com.jd.auction.common.core.constant.SQLType;
import com.jd.auction.common.core.exception.SQLParsingException;
import com.jd.auction.common.core.parsing.lexer.Lexer;
import com.jd.auction.common.core.parsing.lexer.analyzer.Dictionary;
import com.jd.auction.common.core.parsing.lexer.token.Assist;
import com.jd.auction.common.core.parsing.lexer.token.DefaultKeyword;
import com.jd.auction.common.core.parsing.lexer.token.Keyword;
import com.jd.auction.common.core.parsing.lexer.token.TokenType;

public final class SQLJudgeEngine {
    
    private final String sql;

    public SQLJudgeEngine(String sql) {
        this.sql = sql;
    }

    public SQLType judgeSQLType() {
        Lexer lexer = new Lexer(sql, new Dictionary());
        lexer.nextToken();
        while (true) {
            TokenType tokenType = lexer.getCurrentToken().getType();
            if (tokenType instanceof Keyword) {
                if (DefaultKeyword.SELECT == tokenType) {
                    return SQLType.DQL;
                }
                if (DefaultKeyword.INSERT == tokenType || DefaultKeyword.UPDATE == tokenType || DefaultKeyword.DELETE == tokenType) {
                    return SQLType.DML;
                }
                if (DefaultKeyword.CREATE == tokenType || DefaultKeyword.ALTER == tokenType || DefaultKeyword.DROP == tokenType || DefaultKeyword.TRUNCATE == tokenType) {
                    return SQLType.DDL;
                }
            }
            if (tokenType instanceof Assist && Assist.END == tokenType) {
                throw new SQLParsingException("Unsupported SQL statement: [%s]", sql);
            }
            lexer.nextToken();
        }
    }
}
