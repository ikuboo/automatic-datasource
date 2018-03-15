package com.jd.auction.common.automatic.datasouce;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


public final class NamedDataSource {
    
    private final String name;
    private final DataSource dataSource;
    private final Integer weight;

    public NamedDataSource(String name, DataSource dataSource) {
        this(name, dataSource, null);
    }

    public NamedDataSource(String name, DataSource dataSource, Integer weight) {
        this.name = name;
        this.dataSource = dataSource;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Integer getWeight() {
        return weight;
    }
}
