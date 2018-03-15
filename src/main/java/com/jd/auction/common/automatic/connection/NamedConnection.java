package com.jd.auction.common.automatic.connection;

import java.sql.Connection;

/**
 * @author yuanchunsen@jd.com
 *         2018/3/15.
 */
public class NamedConnection {
    private String dataSourceName;
    private Connection connection;

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
