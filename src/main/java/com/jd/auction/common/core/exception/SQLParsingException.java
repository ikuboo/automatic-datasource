package com.jd.auction.common.core.exception;

import com.jd.auction.common.core.parsing.lexer.Lexer;
import com.jd.auction.common.core.parsing.lexer.token.TokenType;

/**
 * @author yuanchunsen@jd.com
 *         2018/3/9.
 */
public final class SQLParsingException extends AutomaticDatasourceException {

    private static final long serialVersionUID = -6408790652103666096L;

    private static final String UNMATCH_MESSAGE = "SQL syntax error, expected token is '%s', actual token is '%s', literals is '%s'.";

    private static final String TOKEN_ERROR_MESSAGE = "SQL syntax error, token is '%s', literals is '%s'.";

    public SQLParsingException(final String message, final Object... args) {
        super(message, args);
    }

    public SQLParsingException(final Lexer lexer, final TokenType expectedTokenType) {
        super(String.format(UNMATCH_MESSAGE, expectedTokenType, lexer.getCurrentToken().getType(), lexer.getCurrentToken().getLiterals()));
    }

}
