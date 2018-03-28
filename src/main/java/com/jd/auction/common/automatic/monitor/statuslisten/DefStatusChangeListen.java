package com.jd.auction.common.automatic.monitor.statuslisten;

import com.jd.auction.common.automatic.datasouce.NamedDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuanchunsen@jd.com
 *         2018/3/16.
 */
public class DefStatusChangeListen extends AbstractStatusChangeListen {
    private static final Logger logger = LoggerFactory.getLogger(DefStatusChangeListen.class);

    @Override
    public void changeToAvailable(final NamedDataSource namedDataSource) {
        logger.error(String.format("数据源[%s]恢复正常!", namedDataSource.getName()));
    }

    @Override
    public void changeToUnavailable(final NamedDataSource namedDataSource) {
        logger.error(String.format("数据源[%s]故障!", namedDataSource.getName()));
    }
}
