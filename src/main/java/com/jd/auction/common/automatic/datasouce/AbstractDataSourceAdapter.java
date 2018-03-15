package com.jd.auction.common.automatic.datasouce;

import com.google.common.base.Preconditions;
import com.jd.auction.common.automatic.constant.DatabaseType;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author yuanchunsen@jd.com
 *         2018/3/9.
 */
public abstract class AbstractDataSourceAdapter extends AbstractUnsupportedOperationDataSource {

    private  DatabaseType databaseType;

    private PrintWriter logWriter = new PrintWriter(System.out);

    @Override
    public final PrintWriter getLogWriter() throws SQLException {
        return logWriter;
    }

    @Override
    public final void setLogWriter(final PrintWriter out) throws SQLException {
        this.logWriter = out;
    }

    @Override
    public final Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    @Override
    public final Connection getConnection(final String username, final String password) throws SQLException {
        return getConnection();
    }

    public DatabaseType getDatabaseType() throws SQLException {
        Collection<NamedDataSource> allDataSource = getAllDataSource();
        DatabaseType result = null;
        for (NamedDataSource each : allDataSource) {
            DatabaseType databaseType = getDatabaseType(each.getDataSource());
            Preconditions.checkState(null == result || result.equals(databaseType), String.format("Database type inconsistent with '%s' and '%s'", result, databaseType));
            result = databaseType;
        }
        return result;
    }

    private DatabaseType getDatabaseType(final DataSource dataSource) throws SQLException {
        if (dataSource instanceof AbstractDataSourceAdapter) {
            return ((AbstractDataSourceAdapter) dataSource).databaseType;
        }
        try (Connection connection = dataSource.getConnection()) {
            return DatabaseType.valueFrom(connection.getMetaData().getDatabaseProductName());
        }
    }

    protected abstract List<NamedDataSource> getAllDataSource();
}
