package com.jd.auction.common.automatic.connection;

import com.jd.auction.common.automatic.datasouce.NamedDataSource;

import java.sql.Connection;

/**
 * @author yuanchunsen@jd.com
 *         2018/3/15.
 */
public class NamedConnection {
    private  final Connection connection;
    private  final NamedDataSource NamedDataSource;
    private  final String  dataSourceName;

    public NamedConnection(Connection connection, com.jd.auction.common.automatic.datasouce.NamedDataSource namedDataSource, String dataSourceName) {
        this.connection = connection;
        NamedDataSource = namedDataSource;
        this.dataSourceName = dataSourceName;
    }

    public Connection getConnection() {
        return connection;
    }

    public com.jd.auction.common.automatic.datasouce.NamedDataSource getNamedDataSource() {
        return NamedDataSource;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }
}
