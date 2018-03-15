
package com.jd.auction.common.core.statement;

import com.google.common.base.Preconditions;
import com.jd.auction.common.core.connection.MasterSlaveConnection;
import com.jd.auction.common.core.constant.SQLType;
import com.jd.auction.common.core.parsing.SQLJudgeEngine;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class MasterSlaveStatement extends AbstractStatementAdapter {

    private final MasterSlaveConnection masterSlaveConnection;

    private final int resultSetType;

    private final int resultSetConcurrency;

    private final int resultSetHoldability;

    private Statement currentStatement;


    public MasterSlaveStatement(final MasterSlaveConnection masterSlaveConnection) {
        this(masterSlaveConnection, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }

    public MasterSlaveStatement(final MasterSlaveConnection masterSlaveConnection, final int resultSetType, final int resultSetConcurrency) {
        this(masterSlaveConnection, resultSetType, resultSetConcurrency, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }

    public MasterSlaveStatement(final MasterSlaveConnection masterSlaveConnection, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) {
        super(Statement.class);
        this.masterSlaveConnection = masterSlaveConnection;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        this.resultSetHoldability = resultSetHoldability;
    }

    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        Connection connection = masterSlaveConnection.getConnection(sqlType);
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        currentStatement = statement;
        return statement.executeQuery(sql);
    }

    @Override
    public int executeUpdate(final String sql) throws SQLException {
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        Connection connection = masterSlaveConnection.getConnection(sqlType);
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        currentStatement = statement;
        return statement.executeUpdate(sql);
    }

    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        Connection connection = masterSlaveConnection.getConnection(sqlType);
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        currentStatement = statement;
        return statement.executeUpdate(sql, autoGeneratedKeys);

    }

    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        Connection connection = masterSlaveConnection.getConnection(sqlType);
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        currentStatement = statement;
        return statement.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        Connection connection = masterSlaveConnection.getConnection(sqlType);
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        currentStatement = statement;
        return statement.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(final String sql) throws SQLException {
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        Connection connection = masterSlaveConnection.getConnection(sqlType);
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        currentStatement = statement;
        return statement.execute(sql);
    }

    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        Connection connection = masterSlaveConnection.getConnection(sqlType);
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        return statement.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        Connection connection = masterSlaveConnection.getConnection(sqlType);
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        currentStatement = statement;
        return statement.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(final String sql, final String[] columnNames) throws SQLException {
        SQLType sqlType = new SQLJudgeEngine(sql).judgeSQLType();
        Connection connection = masterSlaveConnection.getConnection(sqlType);
        Statement statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        currentStatement = statement;
        return statement.execute(sql, columnNames);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        Preconditions.checkArgument(null != currentStatement);
        return currentStatement.getGeneratedKeys();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        Preconditions.checkArgument(null != currentStatement);
        return currentStatement.getResultSet();
    }

    @Override
    public MasterSlaveConnection getConnection() {
        return masterSlaveConnection;
    }

    @Override
    public int getResultSetType() {
        return resultSetType;
    }

    @Override
    public int getResultSetConcurrency() {
        return resultSetConcurrency;
    }

    @Override
    public int getResultSetHoldability() {
        return resultSetHoldability;
    }

    @Override
    public Statement getCurrentStatements() {
        return currentStatement;
    }


}