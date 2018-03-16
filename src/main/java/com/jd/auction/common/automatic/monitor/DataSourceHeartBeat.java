package com.jd.auction.common.automatic.monitor;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jd.auction.common.automatic.datasouce.NamedDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 数据源心跳
 *
 * @author yuanchunsen@jd.com
 *         2018/3/15.
 */
public class DataSourceHeartBeat {
    private DataSourceHeartBeat() {
    }

    private static final Logger logger = LoggerFactory.getLogger(DataSourceHeartBeat.class);
    private static ScheduledExecutorService executorService;

    /**
     * 初始化
     *
     * @param heartBeatPeriod 心跳间隔，秒
     */
    public static void init(Integer heartBeatPeriod) {
        Preconditions.checkNotNull(heartBeatPeriod);
        Preconditions.checkArgument(heartBeatPeriod > 0, "heartBeatPeriod mast > 0");
        executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(false).setNameFormat("automatic-datasource-heartbeat-%d").build());

        executorService.scheduleWithFixedDelay(new HeartBeadTask(), 10, heartBeatPeriod, TimeUnit.SECONDS);
    }

    public static void destory() {
        if (null != executorService) {
            //关闭线程池
            executorService.shutdownNow();
        }
    }
}

class HeartBeadTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HeartBeadTask.class);

    @Override
    public void run() {
        List<NamedDataSource> unAvailableDataSource = DataSourceStateJudge.getUnAvailableDataSource();
        if (unAvailableDataSource.size() <= 0) {
            return;
        }

        for (NamedDataSource namedDataSource : unAvailableDataSource) {
            logger.error("心跳数据源:" + namedDataSource.getName());
            FutureTask<Boolean> futureTask = new FutureTask<Boolean>(new MyCallAble(namedDataSource));
            new Thread(futureTask).start();
            try {
                Boolean aBoolean = futureTask.get(5, TimeUnit.SECONDS);
                if (aBoolean) {
                    DataSourceStateJudge.changeToAvailable(namedDataSource);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                logger.error("0--------------------------------------------------------------超时");
            }
        }
    }
}


class MyCallAble implements Callable<Boolean> {
    private final NamedDataSource namedDataSource;

    public MyCallAble(final NamedDataSource namedDataSource) {
        this.namedDataSource = namedDataSource;
    }

    @Override
    public Boolean call() throws Exception {
        DataSource dataSource = namedDataSource.getDataSource();
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        } else {
            try {
                Connection connection = dataSource.getConnection();
                connection.createStatement().executeQuery("select 1");
            } catch (SQLException e) {
                return false;
            }
            return true;
        }
        return true;
    }
}
