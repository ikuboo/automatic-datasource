package com.jd.auction.common.automatic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 测试数据源出错抛出的异常
 *
 * @author yuanchunsen@jd.com
 *         2018/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestJdbcException {

    @Resource(name = "dataSource_m")
    private DataSource m_datasource;
    @Resource(name = "dbcp_m")
    private DataSource dbcp_m;

    @Test
    public void exceptionTest() throws InterruptedException {

        while (true) {
            try {
                Connection connection = dbcp_m.getConnection();
                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("select * from t_user where id = 1");
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    System.out.println(String.format("id={%d},name={%s}", id, name));
                }                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Thread.sleep(1000);
        }
    }
}

