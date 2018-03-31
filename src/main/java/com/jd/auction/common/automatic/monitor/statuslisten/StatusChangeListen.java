package com.jd.auction.common.automatic.monitor.statuslisten;

import com.jd.auction.common.automatic.datasouce.NamedDataSource;

/**
 * 数据源状态变化监听器
 * @author yuanchunsen@jd.com
 *         2018/3/16.
 */
public interface StatusChangeListen {
    /**
     * 数据源变为可用
     */
    void changeToAvailable(final NamedDataSource namedDataSource);
    /**
     * 数据源变为不可用
     */
    void changeToUnavailable(final NamedDataSource namedDataSource);
}
