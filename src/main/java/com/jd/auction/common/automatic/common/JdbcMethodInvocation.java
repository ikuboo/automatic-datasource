package com.jd.auction.common.automatic.api;

import com.jd.auction.common.automatic.exception.AutomaticDatasourceException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class JdbcMethodInvocation {
    

    private final Method method;
    private final Object[] arguments;

    public JdbcMethodInvocation(Method method, Object[] arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    /**
     * Invoke JDBC method.
     * 
     * @param target target object
     */
    public void invoke(final Object target) {
        try {
            method.invoke(target, arguments);
        } catch (final IllegalAccessException | InvocationTargetException ex) {
            throw new AutomaticDatasourceException("Invoke jdbc method exception", ex);
        }
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
