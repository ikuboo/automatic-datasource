package com.jd.auction.common.automatic.monitor.statuslisten;

import com.jd.auction.common.automatic.datasouce.NamedDataSource;

/**
 * @author yuanchunsen@jd.com
 *         2018/3/16.
 */
public  abstract class AbstractStatusChangeListen implements StatusChangeListen {
    @Override
    public void changeToAvailable(final NamedDataSource namedDataSource) {

    }

    @Override
    public void changeToUnavailable(final NamedDataSource namedDataSource) {

    }
}
