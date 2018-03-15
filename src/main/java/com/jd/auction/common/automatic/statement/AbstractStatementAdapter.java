package com.jd.auction.common.core.statement;


import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;


public abstract class AbstractStatementAdapter extends AbstractUnsupportedOperationStatement {
    
    private final Class<? extends Statement> targetClass;
    
    private boolean closed;
    
    private boolean poolable;
    
    private int fetchSize;


    public AbstractStatementAdapter(Class<? extends Statement> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public final void close() throws SQLException {
        closed = true;
        getCurrentStatements().close();
    }
    
    @Override
    public final boolean isClosed() throws SQLException {
        return closed;
    }
    
    @Override
    public final boolean isPoolable() throws SQLException {
        return poolable;
    }
    
    @Override
    public final void setPoolable(final boolean poolable) throws SQLException {
        this.poolable = poolable;
        getCurrentStatements().setPoolable(poolable);

    }
    
    @Override
    public final int getFetchSize() throws SQLException {
        return fetchSize;
    }
    
    @Override
    public final void setFetchSize(final int rows) throws SQLException {
        this.fetchSize = rows;
        getCurrentStatements().setFetchSize(rows);
    }
    
    @Override
    public final void setEscapeProcessing(final boolean enable) throws SQLException {
        getCurrentStatements().setEscapeProcessing(enable);
    }
    
    @Override
    public final void cancel() throws SQLException {
        getCurrentStatements().cancel();
    }
    
    @Override
    public final int getUpdateCount() throws SQLException {
        long result = getCurrentStatements().getUpdateCount();

        if (result > Integer.MAX_VALUE) {
            result = Integer.MAX_VALUE;
        }
        return Long.valueOf(result).intValue();
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }
    
    @Override
    public void clearWarnings() throws SQLException {
    }
    
    @Override
    public final boolean getMoreResults() throws SQLException {
        return false;
    }
    
    @Override
    public final boolean getMoreResults(final int current) throws SQLException {
        return false;
    }
    
    @Override
    public final int getMaxFieldSize() throws SQLException {
        return getCurrentStatements().getMaxFieldSize();
    }
    
    @Override
    public final void setMaxFieldSize(final int max) throws SQLException {
        getCurrentStatements().setMaxFieldSize(max);
    }

    @Override
    public final int getMaxRows() throws SQLException {
        return getCurrentStatements().getMaxRows();
    }
    
    @Override
    public final void setMaxRows(final int max) throws SQLException {
        getCurrentStatements().setMaxRows(max);
    }
    
    @Override
    public final int getQueryTimeout() throws SQLException {
        return getCurrentStatements().getQueryTimeout();
    }
    
    @Override
    public final void setQueryTimeout(final int seconds) throws SQLException {
        getCurrentStatements().setQueryTimeout(seconds);
    }

    protected abstract Statement getCurrentStatements();
}
