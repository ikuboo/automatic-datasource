package com.jd.auction.common.core.parsing.lexer.analyzer;


import com.jd.auction.common.core.exception.AutomaticDatasourceException;


public final class UnterminatedCharException extends AutomaticDatasourceException {
    
    private static final long serialVersionUID = 8575890835166900925L;
    
    private static final String MESSAGE = "Illegal input, unterminated '%s'.";
    
    public UnterminatedCharException(final char terminatedChar) {
        super(String.format(MESSAGE, terminatedChar));
    }
    
    public UnterminatedCharException(final String terminatedChar) {
        super(String.format(MESSAGE, terminatedChar));
    }
}
