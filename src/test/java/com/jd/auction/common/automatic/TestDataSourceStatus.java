package com.jd.auction.common.automatic;

import jdk.nashorn.internal.codegen.CompilerConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.*;

/**
 * 测试数据源出错抛出的异常
 *
 * @author yuanchunsen@jd.com
 *         2018/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-datasource.xml"})
public class TestDataSourceStatus {
    private static final Logger logger = LoggerFactory.getLogger(TestDataSourceStatus.class);
    @Resource(name = "druidDataSource")
    private DataSource druidDataSource;
    @Resource(name = "dbcpDataSource")
    private DataSource dbcpDataSource;

    @Test
    public void exceptionTest() throws InterruptedException {
        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new MyRunAble(executorService, druidDataSource), 1, 1, TimeUnit.SECONDS);

        TimeUnit.HOURS.sleep(1);
    }
}

class MyRunAble implements Runnable{

    private final ExecutorService executorService;
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(MyCallAble.class);
    public MyRunAble(ExecutorService executorService, DataSource dataSource) {
        this.executorService = executorService;
        this.dataSource = dataSource;
    }

    @Override
    public void run() {
        Future<Boolean> submit = executorService.submit(new MyCallAble(executorService, dataSource));
        try {
            Boolean aBoolean = submit.get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("----------------------出错2----------!");
        }
    }
}

class MyCallAble implements Callable<Boolean>{
    private final ExecutorService executorService;
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(MyCallAble.class);
    public MyCallAble(ExecutorService executorService, DataSource dataSource) {
        this.executorService = executorService;
        this.dataSource = dataSource;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            //Connection connection = dbcpDataSource.getConnection();
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.executeQuery("select 1");
            statement.close();
            connection.close();
            logger.info("-----------------------正常----------!");
            return true;
        } catch (Exception e) {
            logger.error("----------------------出错1----------!");
        }
        return false;
    }
}

