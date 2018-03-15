

package com.jd.auction.common.automatic.preparedstatement;

import com.google.common.base.Preconditions;
import com.jd.auction.common.automatic.connection.MasterSlaveConnection;
import com.jd.auction.common.automatic.connection.NamedConnection;
import com.jd.auction.common.automatic.constant.SQLType;
import com.jd.auction.common.automatic.parsing.SQLJudgeEngine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public final class MasterSlavePreparedStatement extends AbstractMasterSlavePreparedStatementAdapter {

    private final MasterSlaveConnection masterSlaveConnection;

    private final PreparedStatement currentPreparedStatements;

    public MasterSlavePreparedStatement(final MasterSlaveConnection masterSlaveConnection, final String sql) throws SQLException {
        this(masterSlaveConnection, sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }

    public MasterSlavePreparedStatement(final MasterSlaveConnection masterSlaveConnection, final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        this(masterSlaveConnection, sql, resultSetType, resultSetConcurrency, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }

    public MasterSlavePreparedStatement(final MasterSlaveConnection masterSlaveConnection, final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        this.masterSlaveConnection = masterSlaveConnection;
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        NamedConnection namedConnection = masterSlaveConnection.getConnection(sqlType);
        Connection connection = namedConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        this.currentPreparedStatements = preparedStatement;
    }

    public MasterSlavePreparedStatement(final MasterSlaveConnection masterSlaveConnection, final String sql, final int autoGeneratedKeys) throws SQLException {
        this.masterSlaveConnection = masterSlaveConnection;
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        NamedConnection namedConnection = masterSlaveConnection.getConnection(sqlType);
        Connection connection = namedConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, autoGeneratedKeys);
        this.currentPreparedStatements = preparedStatement;
    }

    public MasterSlavePreparedStatement(final MasterSlaveConnection masterSlaveConnection, final String sql, final int[] columnIndexes) throws SQLException {
        this.masterSlaveConnection = masterSlaveConnection;
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        NamedConnection namedConnection = masterSlaveConnection.getConnection(sqlType);
        Connection connection = namedConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, columnIndexes);
        this.currentPreparedStatements = preparedStatement;
    }

    public MasterSlavePreparedStatement(final MasterSlaveConnection masterSlaveConnection, final String sql, final String[] columnNames) throws SQLException {
        this.masterSlaveConnection = masterSlaveConnection;
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        NamedConnection namedConnection = masterSlaveConnection.getConnection(sqlType);
        Connection connection = namedConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, columnNames);
        this.currentPreparedStatements = preparedStatement;

    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        Preconditions.checkArgument(currentPreparedStatements != null,"Cannot support executeQuery for DDL");
        return currentPreparedStatements.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        return currentPreparedStatements.executeUpdate();
    }

    @Override
    public boolean execute() throws SQLException {
        return  currentPreparedStatements.execute();
    }

    @Override
    public void clearBatch() throws SQLException {
        currentPreparedStatements.clearBatch();
    }

    @Override
    public void addBatch() throws SQLException {
        currentPreparedStatements.addBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return  currentPreparedStatements.executeBatch();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return  currentPreparedStatements.getResultSet();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return  currentPreparedStatements.getGeneratedKeys();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return  currentPreparedStatements.getResultSetHoldability();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return  currentPreparedStatements.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return  currentPreparedStatements.getResultSetType();
    }

    @Override
    public MasterSlaveConnection getConnection() {
        return masterSlaveConnection;
    }

    @Override
    public PreparedStatement getCurrentStatements() {
        return currentPreparedStatements;
    }
}
