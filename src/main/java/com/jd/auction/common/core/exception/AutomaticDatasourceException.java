package com.jd.auction.common.core.exception;

/**
 * Base exception for AutoMaticDataSource
 * @author yuanchunsen@jd.com
 *         2018/3/9.
 */
public class AutomaticDatasourceException extends RuntimeException {
    private static final long serialVersionUID = -1343739516839252250L;

    /**
     * Constructs an exception with formatted error message and arguments.
     *
     * @param errorMessage formatted error message
     * @param args arguments of error message
     */
    public AutomaticDatasourceException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    /**
     * Constructs an exception with error message and cause.
     *
     * @param message error message
     * @param cause error cause
     */
    public AutomaticDatasourceException(final String message, final Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs an exception with cause.
     *
     * @param cause error cause
     */
    public AutomaticDatasourceException(final Exception cause) {
        super(cause);
    }
}
