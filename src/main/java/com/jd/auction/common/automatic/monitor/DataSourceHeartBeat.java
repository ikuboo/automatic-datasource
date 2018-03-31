package com.jd.auction.common.automatic.monitor;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jd.auction.common.automatic.datasouce.NamedDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
    private static ScheduledExecutorService scheduledExecutorService;
    private static ExecutorService executorService;

    /**
     * 初始化
     *
     * @param heartBeatPeriod 心跳间隔，秒
     */
    public static void init(Integer heartBeatPeriod) {
        Preconditions.checkNotNull(heartBeatPeriod);
        Preconditions.checkArgument(heartBeatPeriod > 0, "heartBeatPeriod mast > 0");

        //数据源心跳线程池
        executorService = Executors.newFixedThreadPool(5, new ThreadFactoryBuilder().setDaemon(false)
                .setNameFormat("automatic-datasource-heartbeat-%d").build());
        //检查故障数据源的定时任务线程池
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(false)
                .setNameFormat("automatic-datasource-statuscheck-%d").build());
        //延迟10秒开始执行
        scheduledExecutorService.scheduleWithFixedDelay(new ValidateDataSourceTask(executorService), 10, heartBeatPeriod, TimeUnit.SECONDS);
    }

    public static void destory() {
        if (null != executorService) {
            //关闭线程池
            executorService.shutdownNow();
        }
        if(null != scheduledExecutorService){
            scheduledExecutorService.shutdownNow();
        }
    }
}

class ValidateDataSourceTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ValidateDataSourceTask.class);
    private final ExecutorService executorService;

    public ValidateDataSourceTask(final ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void run() {
        List<NamedDataSource> unAvailableDataSource = DataSourceStateJudge.getUnAvailableDataSource();
        if (unAvailableDataSource.size() <= 0) {
            return;
        }

        for (NamedDataSource namedDataSource : unAvailableDataSource) {
            Future<Boolean> validateCall = executorService.submit(new HeartBeatCallAble(namedDataSource));
            try {
                Boolean validateResult = validateCall.get(5, TimeUnit.SECONDS);
                if (validateResult) {
                    DataSourceStateJudge.changeToAvailable(namedDataSource);
                    logger.debug(String.format("心跳数据源[%s]成功!", namedDataSource.getName()));
                }
            } catch (TimeoutException e) {
                logger.debug(String.format("心跳数据源[%s]超时!",namedDataSource.getName()));
                validateCall.cancel(true);
            }   catch (InterruptedException | ExecutionException e) {
                logger.debug(String.format("心跳数据源[%s]被中断!",namedDataSource.getName()));
            }
        }
    }
}


class HeartBeatCallAble implements Callable<Boolean>{
    private final NamedDataSource namedDataSource;
    public HeartBeatCallAble( final NamedDataSource namedDataSource) {
        this.namedDataSource = namedDataSource;
    }

    @Override
    public Boolean call() {
        try {
            Connection connection = namedDataSource.getDataSource().getConnection();
            Statement statement = connection.createStatement();
            statement.executeQuery("select 1");
            statement.close();
            connection.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}