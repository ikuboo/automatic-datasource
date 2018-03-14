

package com.jd.auction.common.core.connection;


import com.jd.auction.common.core.constant.SQLType;
import com.jd.auction.common.core.datasouce.AutoMaticDataSource;
import com.jd.auction.common.core.datasouce.NamedDataSource;
import com.jd.auction.common.core.statement.MasterSlavePreparedStatement;
import com.jd.auction.common.core.statement.MasterSlaveStatement;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public final class MasterSlaveConnection extends AbstractConnectionAdapter {
    private final AutoMaticDataSource autoMaticDataSource;
    private SQLType cachedSQLType;


    public MasterSlaveConnection(AutoMaticDataSource autoMaticDataSource) {
        this.autoMaticDataSource = autoMaticDataSource;
    }

    public Connection getConnection(final SQLType sqlType) throws SQLException {
        cachedSQLType = sqlType;
        NamedDataSource namedDataSource = autoMaticDataSource.getDataSource(sqlType);
        return getCachedConnection(namedDataSource);
    }
    
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return getConnection(null == cachedSQLType ? SQLType.DML : cachedSQLType).getMetaData();
    }
    
    @Override
    public Statement createStatement() throws SQLException {
        return new MasterSlaveStatement(this);
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
        return new MasterSlaveStatement(this, resultSetType, resultSetConcurrency);
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        return new MasterSlaveStatement(this, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql, resultSetType, resultSetConcurrency);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql, autoGeneratedKeys);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql, columnIndexes);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql, columnNames);
    }
    
    @Override
    public void close() throws SQLException {
        AutoMaticDataSource.resetDMLFlag();
        super.close();
    }
}
