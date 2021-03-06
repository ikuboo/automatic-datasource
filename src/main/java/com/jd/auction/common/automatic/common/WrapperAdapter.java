package com.jd.auction.common.automatic.common;

import com.jd.auction.common.automatic.exception.AutomaticDatasourceException;

import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collection;

public class WrapperAdapter implements Wrapper {
    
    private final Collection<JdbcMethodInvocation> jdbcMethodInvocations = new ArrayList<>();
    
    @SuppressWarnings("unchecked")
    @Override
    public final <T> T unwrap(final Class<T> iface) throws SQLException {
        if (isWrapperFor(iface)) {
            return (T) this;
        }
        throw new SQLException(String.format("[%s] cannot be unwrapped as [%s]", getClass().getName(), iface.getName()));
    }
    
    @Override
    public final boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }
    
    /**
     * 记录方法调用.
     * 
     * @param targetClass 目标类
     * @param methodName 方法名称
     * @param argumentTypes 参数类型
     * @param arguments 参数
     */
    public final void recordMethodInvocation(final Class<?> targetClass, final String methodName, final Class<?>[] argumentTypes, final Object[] arguments) {
        try {
            jdbcMethodInvocations.add(new JdbcMethodInvocation(targetClass.getMethod(methodName, argumentTypes), arguments));
        } catch (final NoSuchMethodException ex) {
            throw new AutomaticDatasourceException(ex);
        }
    }
    
    /**
     * 回放记录的方法调用.
     * 
     * @param target 目标对象
     */
    public final void replayMethodsInvocation(final Object target) {
        for (JdbcMethodInvocation each : jdbcMethodInvocations) {
            each.invoke(target);
        }
    }

    /**
     * 合并sql异常链
     * @param exceptions 异常链
     * @throws SQLException 合并后的异常
     */
    protected void throwSQLExceptionIfNecessary(final Collection<SQLException> exceptions) throws SQLException {
        if (exceptions.isEmpty()) {
            return;
        }
        SQLException ex = new SQLException();
        for (SQLException each : exceptions) {
            ex.setNextException(each);
        }
        throw ex;
    }
}
