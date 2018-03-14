package com.jd.auction.common.core.datasouce;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


public final class NamedDataSource {
    
    private final String name;
    
    private final DataSource dataSource;

    public NamedDataSource(String name, DataSource dataSource) {
        this.name = name;
        this.dataSource = dataSource;
    }

    public Map<String, DataSource> toMap() {
        Map<String, DataSource> result = new HashMap<>(1, 1);
        result.put(name, dataSource);
        return result;
    }

    public String getName() {
        return name;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
